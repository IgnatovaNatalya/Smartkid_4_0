package ru.mamsikgames.smartkid.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName

interface SmartRepository {
    fun addUser(strName:String): Completable
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

    fun getListOperations(): Flowable<List<OperationEntity>>
    fun getOperation(operationId:Int): Single<OperationEntity>
    fun getCountOperations(): Flowable<Int>
    fun insertOperation(op: OperationEntity) : Completable


}