package com.alcntml.codecase.di

import com.alcntml.codecase.ui.search.SearchFragment
import com.alcntml.codecase.ui.userrepo.UserRepoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeUserRepoFragment(): UserRepoFragment
}
