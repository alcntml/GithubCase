package com.alcntml.codecase.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.alcntml.codecase.repository.UserRepoRepository
import com.alcntml.codecase.testing.OpenForTesting
import com.alcntml.codecase.util.AbsentLiveData
import com.alcntml.codecase.vo.Resource
import com.alcntml.codecase.vo.UserRepo
import java.util.*
import javax.inject.Inject

@OpenForTesting
class SearchViewModel @Inject constructor(repoRepository: UserRepoRepository) : ViewModel() {

    private val _query = MutableLiveData<String>()

    val query : LiveData<String> = _query

    val results: LiveData<Resource<List<UserRepo>>> = Transformations
        .switchMap(_query) { search ->
            if (search.isNullOrBlank()) {
                AbsentLiveData.create()
            } else {
                repoRepository.search(search)
            }
        }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        _query.value = input
    }

    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
    }

}
