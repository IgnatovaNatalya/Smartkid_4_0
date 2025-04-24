package ru.mamsikgames.smartkid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "GroupLevel")
data class GroupLevelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val name: String = ""
)