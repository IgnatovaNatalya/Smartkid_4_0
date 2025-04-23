package ru.mamsikgames.smartkid.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import ru.mamsikgames.smartkid.data.dao.SmartDao
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.SmartRepository

class SmartRepositoryImpl(private val smartDao: SmartDao) : SmartRepository {

    override fun addUser(strName: String): Completable {
        return smartDao.addUser(strName)
    }

    override fun insertRound(r: RoundEntity): Single<Long> {
        return smartDao.insertRound(r)
    }

    override fun updateRound(r: RoundEntity): Completable {
        return smartDao.updateRound(r)
    }

    override fun insertOperation(op: OperationEntity): Completable {
        return smartDao.insertOperation(op)
    }

    override fun setCurrentUser(userId: Int): Completable {
        return smartDao.setCurrentUser(userId)
    }

    override fun getListRoundsWithNames(userId: Int): Flowable<List<RoundWithName>> {
        return smartDao.getListRoundsWithNames(userId)
    }

    override fun getPendingRound(userId: Int, operationId: Int): Maybe<RoundEntity> {
        return smartDao.getPendingRound(userId, operationId)
    }

    override fun getRate(userId: Int): Flowable<Int> {
        return smartDao.getRate(userId)
    }

    override fun getRates(userId: Int): Single<List<Rate>> {
        return smartDao.getRates(userId)
    }

    override fun getLeaders(): Flowable<List<Leader>> {
        return smartDao.getLeaders()
    }

    override fun getListUsers(): Flowable<List<UserEntity>> {
        return smartDao.getListUsers()
    }

    override fun getCurrentUser(): Flowable<UserEntity> {
        return smartDao.getCurrentUser()
    }

//    fun getCurUser(): UserEntity {
//        return smartDao.getCurUser()
//    }

    override fun getListOperations(): Flowable<List<OperationEntity>> {
        return smartDao.getListOperations()
    }

    override fun getCountOperations(): Flowable<Int> {
        return smartDao.getCountOperations()
    }

    override fun getOperation(operationId: Int): Single<OperationEntity> {
        return smartDao.getOperation(operationId)
    }
}