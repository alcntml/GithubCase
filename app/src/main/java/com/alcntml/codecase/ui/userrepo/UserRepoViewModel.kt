package com.alcntml.codecase.ui.userrepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.alcntml.codecase.repository.UserRepoRepository
import com.alcntml.codecase.util.AbsentLiveData
import com.alcntml.codecase.vo.Resource
import com.alcntml.codecase.vo.UserRepo
import javax.inject.Inject

class UserRepoViewModel @Inject constructor(repository: UserRepoRepository) : ViewModel() {
    private val _repoId: MutableLiveData<RepoId> = MutableLiveData()
    val repo: LiveData<Resource<UserRepo>> = Transformations
            .switchMap(_repoId) { input ->
                input.ifExists { owner, name ->
                    repository.loadRepo(owner, name)
                }
            }

    fun retry() {
        val owner = _repoId.value?.owner
        val name = _repoId.value?.name
        if (owner != null && name != null) {
            _repoId.value = RepoId(owner, name)
        }
    }

    fun setId(owner: String, name: String) {
        val update = RepoId(owner, name)
        if (_repoId.value == update) {
            return
        }
        _repoId.value = update
    }

    data class RepoId(val owner: String, val name: String) {
        fun <T> ifExists(f: (String, String) -> LiveData<T>): LiveData<T> {
            return if (owner.isBlank() || name.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(owner, name)
            }
        }
    }
}
