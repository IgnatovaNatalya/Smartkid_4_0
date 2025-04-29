package ru.mamsikgames.smartkid.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName

interface SmartRepository {
    //fun addUser(strName:String): Completable
    fun setCurrentUser(userId:Int) : Completable

    fun getListUsers(): Flowable<List<UserEntity>>
    fun getCurrentUser(): Flowable<UserEntity>


    fun insertRound(r: RoundEntity) : Single<Long>
    fun updateRound(r: RoundEntity) : Completable
    fun getListRoundsWithNames(userId:Int): Flowable<List<RoundWithName>>
    fun getPendingRound(userId:Int, operationId:Int): Maybe<RoundEntity>

    fun getRate(userId:Int): Flowable<Int>
    fun getRates(userId:Int): Single<List<Rate>>

    fun getLeaders(): Flowable<List<Leader>>

    fun getListLevelsAndGroups(): Flowable<List<LevelModel>>
    fun getLevelParams(operationId:Int): Single<LevelParams>
    fun getCountLevels(): Flowable<Int>
   //fun insertLevel(op: LevelEntity) : Completable

    //fun getListLevelGroups(): Flowable<List<LevelGroupEntity>>

}