package ru.mamsikgames.smartkid.domain.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Task

interface GameInteractor {
    fun getLevelParams(id:Int): Single<LevelParams>
    fun insertRound(r:RoundEntity): Single<Long>
    fun updateRound(r: RoundEntity): Completable
    fun getPendingRound(userId: Int, levelId: Int): Maybe<RoundEntity>
    fun getTask(levelParams:LevelParams):Task
    fun newTask(levelParams:LevelParams): Task
}