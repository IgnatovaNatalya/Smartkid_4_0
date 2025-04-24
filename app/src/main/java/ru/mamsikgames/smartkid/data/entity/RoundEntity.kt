package ru.mamsikgames.smartkid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.io.Serializable

@Entity(tableName = "Round")
data class RoundEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var userId: Int = 0,
    var levelId: Int? = 0,
    var roundBegin: Long = 0L,
    var roundEnd: Long = 0L,
    var finished: Boolean = false,
    var duration: Long,
    var numTasks: Int = 0,
    var numEfforts: Int = 0,
    var numCorrect: Int = 0,
    var numWrong: Int = 0,
    var numExits: Int = 0,
) : Serializable