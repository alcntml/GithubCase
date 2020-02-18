package com.alcntml.codecase.vo

import androidx.room.Entity
import androidx.room.TypeConverters
import com.alcntml.codecase.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
data class RepoSearchResult(
    val query: String,
    val repoIds: List<Int>
)
