package ru.mamsikgames.smartkid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey//(autoGenerate = true)
    var userId: Int = 0,
    var userName: String = "",
    var isCurrent: Boolean = false
) : Serializable
