package ru.mamsikgames.smartkid.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import ru.mamsikgames.smartkid.data.db.dao.SmartDao
import ru.mamsikgames.smartkid.data.db.entity.Operation
import ru.mamsikgames.smartkid.data.db.entity.Round
import ru.mamsikgames.smartkid.data.db.entity.User

class SmartRepository (private val smartDao: SmartDao) {

    fun addUser(strName:String): Completable {
        return smartDao.addUser(strName)
    }
    fun insertRound(r: Round) : Single<Long> {
        return smartDao.insertRound(r)
    }

    fun updateRound(r: Round) : Completable {
        return smartDao.updateRound(r)
    }
    fun insertOperation(op: Operation) : Completable {
        return smartDao.insertOperation(op)
    }
    fun setCurrentUser(userId:Int) : Completable {
        return smartDao.setCurrentUser(userId)
    }
    fun getListRoundsWithNames(userId:Int): Flowable<List<RoundWithName>> {
        return smartDao.getListRoundsWithNames(userId)
    }
    fun getPendingRound(userId:Int, operationId:Int): Maybe<Round> {
        return smartDao.getPendingRound(userId, operationId)
    }
    fun getRate(userId:Int): Flowable<Int> {
        return smartDao.getRate(userId)
    }
    fun getRates(userId:Int): Single<List<Rate>> {
        return smartDao.getRates(userId)
    }

    fun getLeaders(): Flowable<List<Leader>> {
        return smartDao.getLeaders()
    }

    fun getListUsers(): Flowable<List<User>> {
        return smartDao.getListUsers()
    }
    fun getCurrentUser(): Flowable<User> {
        return smartDao.getCurrentUser()
    }

    fun getListOperations(): Flowable<List<Operation>> {
        return smartDao.getListOperations()
    }
    fun getCountOperations(): Flowable<Int> {
        return smartDao.getCountOperations()
    }

    fun getOperation(operationId:Int): Single<Operation> {
        return smartDao.getOperation(operationId)
    }
}