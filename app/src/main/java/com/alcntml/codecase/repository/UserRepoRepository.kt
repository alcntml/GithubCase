package com.alcntml.codecase.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.alcntml.codecase.AppExecutors
import com.alcntml.codecase.api.ApiSuccessResponse
import com.alcntml.codecase.api.GithubService
import com.alcntml.codecase.db.GithubDb
import com.alcntml.codecase.db.UserRepoDao
import com.alcntml.codecase.util.AbsentLiveData
import com.alcntml.codecase.util.RateLimiter
import com.alcntml.codecase.vo.RepoSearchResult
import com.alcntml.codecase.vo.Resource
import com.alcntml.codecase.vo.UserRepo
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepoRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: GithubDb,
        private val userRepoDao: UserRepoDao,
        private val githubService: GithubService
) {
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(owner: String): LiveData<Resource<List<UserRepo>>> {
        return object : NetworkBoundResource<List<UserRepo>, List<UserRepo>>(appExecutors) {
            override fun saveCallResult(item: List<UserRepo>) {
                userRepoDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<UserRepo>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner)
            }

            override fun loadFromDb() = userRepoDao.loadRepositories(owner)

            override fun createCall() = githubService.getUserRepos(owner)

            override fun onFetchFailed() {
                repoListRateLimit.reset(owner)
            }
        }.asLiveData()
    }

    fun loadRepo(owner: String, name: String): LiveData<Resource<UserRepo>> {
        return object : NetworkBoundResource<UserRepo, UserRepo>(appExecutors) {
            override fun saveCallResult(item: UserRepo) {
                userRepoDao.insert(item)
            }

            override fun shouldFetch(data: UserRepo?) = data == null

            override fun loadFromDb() = userRepoDao.load(
                    ownerLogin = owner,
                    name = name
            )

            override fun createCall() = githubService.getUserRepo(
                    owner = owner,
                    name = name
            )
        }.asLiveData()
    }

    fun search(query: String): LiveData<Resource<List<UserRepo>>> {
        return object : NetworkBoundResource<List<UserRepo>, List<UserRepo>>(appExecutors) {

            override fun saveCallResult(item: List<UserRepo>) {
                val repoIds = item.map { it.id }
                val repoSearchResult = RepoSearchResult(
                        query = query,
                        repoIds = repoIds
                )
                db.beginTransaction()
                try {
                    userRepoDao.insertRepos(item)
                    userRepoDao.insert(repoSearchResult)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<UserRepo>?) = data == null

            override fun loadFromDb(): LiveData<List<UserRepo>> {
                return Transformations.switchMap(userRepoDao.search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        userRepoDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall() = githubService.getUserRepos(query)

            override fun processResponse(response: ApiSuccessResponse<List<UserRepo>>)
                    : List<UserRepo> {
                return response.body
            }
        }.asLiveData()
    }
}