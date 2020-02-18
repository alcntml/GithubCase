package com.alcntml.codecase.di

import android.app.Application
import androidx.room.Room
import com.alcntml.codecase.BuildConfig.GITHUB_BASE_URL
import com.alcntml.codecase.BuildConfig.ROOM_DB_NAME
import com.alcntml.codecase.api.GithubService
import com.alcntml.codecase.db.GithubDb
import com.alcntml.codecase.db.UserRepoDao
import com.alcntml.codecase.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(GithubService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): GithubDb {
        return Room
            .databaseBuilder(app, GithubDb::class.java, ROOM_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserRepoDao(db: GithubDb): UserRepoDao {
        return db.userRepoDao()
    }
}
