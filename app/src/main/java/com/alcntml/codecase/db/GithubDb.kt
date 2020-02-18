package com.alcntml.codecase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alcntml.codecase.vo.RepoSearchResult
import com.alcntml.codecase.vo.UserRepo

@Database(
    entities = [
        UserRepo::class,
        RepoSearchResult::class],
    version = 3,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userRepoDao(): UserRepoDao
}
