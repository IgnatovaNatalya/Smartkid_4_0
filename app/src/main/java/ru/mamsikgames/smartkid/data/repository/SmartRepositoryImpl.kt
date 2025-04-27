package ru.mamsikgames.smartkid.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import ru.mamsikgames.smartkid.data.dao.SmartDao
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
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

    override fun insertLevel(op: LevelEntity): Completable {
        return smartDao.insertLevel(op)
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

    override fun getListLevels(): Flowable<List<LevelEntity>> {
        return smartDao.getListLevels()
    }

    override fun getCountLevels(): Flowable<Int> {
        return smartDao.getCountLevels()
    }

    override fun getLevel(operationId: Int): Single<LevelEntity> {
        return smartDao.getLevel(operationId)
    }

    override fun getListLevelGroups(): Flowable<List<LevelGroupEntity>> {
        return smartDao.getListLevelGroups()
    }
}