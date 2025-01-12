package com.loicche.todo.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @SerialName("email")
    val email: String,
    @SerialName("full_name")
    val name: String,
    @SerialName("avatar_medium")
    val avatar: String? = null
)


@Serializable
data class FullName(
    @SerialName("full_name")
    val name: String
)


@Serializable
data class Command(
    val uuid: String,
    val type: String,
    val args: FullName
)

@Serializable
data class UserUpdate(
    val commands: List<Command>
) {
    companion object {
        fun getFromName(name: String): UserUpdate {
            return UserUpdate(
                listOf(
                    Command(
                        UUID.randomUUID().toString(),
                        "user_update",
                        FullName(name)
                    )
                )
            )
        }
    }
}