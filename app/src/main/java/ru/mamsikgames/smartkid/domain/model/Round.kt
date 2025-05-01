package ru.mamsikgames.smartkid.domain.model

import ru.mamsikgames.smartkid.data.entity.RoundEntity

data class Round(
    var userId: Int = 0,
    var levelId: Int? = 0,
    var roundBegin: Long = 0L,
    var roundEnd: Long? = 0L,
    var duration: Long? = 0L,
    var numTasks: Int? = 0,
    var numEfforts: Int? = 0,
    var numCorrect: Int? = 0,
    var numWrong: Int? = 0
) {
    companion object {
        fun convertToDb(round: Round): RoundEntity {
            with(round) {
                return RoundEntity(
                    userId = userId,
                    levelId = levelId,
                    roundBegin = roundBegin,
                    roundEnd = roundEnd?.toLong() ?: 0,
                    finished = true,
                    duration = duration?.toLong() ?: 0,
                    numTasks = numTasks?.toInt() ?: 0,
                    numEfforts = numEfforts?.toInt() ?: 0,
                    numCorrect = numCorrect?.toInt() ?: 0,
                    numWrong = numWrong?.toInt() ?: 0
                )
            }
        }
    }
}
