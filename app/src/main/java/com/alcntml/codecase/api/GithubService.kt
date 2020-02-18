package com.alcntml.codecase.api

import androidx.lifecycle.LiveData
import com.alcntml.codecase.vo.UserRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String): LiveData<ApiResponse<List<UserRepo>>>

    @GET("repos/{owner}/{name}")
    fun getUserRepo(
            @Path("owner") owner: String,
            @Path("name") name: String
    ): LiveData<ApiResponse<UserRepo>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): LiveData<ApiResponse<RepoSearchResponse>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Call<RepoSearchResponse>
}
