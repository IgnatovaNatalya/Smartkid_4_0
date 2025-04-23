package ru.mamsikgames.smartkid.domain.model

data class Round(
    var id: Long? = null,
    var userId: Int = 0,
    var operationId: Int? = 0,
    var roundBegin: Long = 0L,
    var roundEnd: Long = 0L,
    var finished: Boolean = false,
    var duration: Long,
    var numTasks: Int = 0,
    var numEfforts: Int = 0,
    var numCorrect: Int = 0,
    var numWrong: Int = 0,
    var numExits: Int = 0,
)
