package com.alcntml.codecase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.alcntml.codecase.ui.search.SearchViewModel
import com.alcntml.codecase.ui.userrepo.UserRepoViewModel
import com.alcntml.codecase.viewmodel.GithubViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserRepoViewModel::class)
    abstract fun bindUserRepoViewModel(userRepoViewModel: UserRepoViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: GithubViewModelFactory): ViewModelProvider.Factory
}
