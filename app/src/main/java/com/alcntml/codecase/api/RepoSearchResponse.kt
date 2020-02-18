package com.alcntml.codecase.api


import com.alcntml.codecase.vo.UserRepo
import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<UserRepo>
) {
    var nextPage: Int? = null
}
