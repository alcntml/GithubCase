package com.alcntml.codecase.util

import com.alcntml.codecase.vo.UserRepo

object TestUtil {

    fun createRepos(count: Int, owner: String, name: String, description: String): List<UserRepo> {
        return (0 until count).map {
            createRepo(
                    owner = owner + it,
                    name = name + it,
                    description = description + it
            )
        }
    }

    fun createRepo(owner: String, name: String, description: String) = createRepo(
            id = UserRepo.UNKNOWN_ID,
            owner = owner,
            name = name,
            description = description
    )

    fun createRepo(id: Int, owner: String, name: String, description: String) = UserRepo(
            id = id,
            name = name,
            fullName = "$owner/$name",
            description = description,
            owner = UserRepo.Owner(owner, null, null),
            stars = 3,
            openIssuesCount = 1,
            favorite = false
            )
}
