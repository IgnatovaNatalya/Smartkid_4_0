package ru.mamsikgames.smartkid.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey//(autoGenerate = true)
    var userId: Int = 0,
    var userName: String = "",
    var avaId: Int = 0,
    var dateBirth: String? = null,
    var current: Boolean = false
) : Serializable
