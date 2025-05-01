package ru.mamsikgames.smartkid.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.domain.model.Leader
import ru.mamsikgames.smartkid.domain.model.Rate
import ru.mamsikgames.smartkid.data.dao.SmartDao
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.domain.model.Round

class SmartRepositoryImpl(private val smartDao: SmartDao) : SmartRepository {

    override fun insertRound(r: RoundEntity): Single<Long> {
        return smartDao.insertRound(r)
    }

    override fun updateRound(r: RoundEntity): Completable {
        return smartDao.updateRound(r)
    }


    override fun setCurrentUser(userId: Int): Completable {
        return smartDao.setCurrentUser(userId)
    }


    override fun getPendingRound(userId: Int, levelId: Int): Maybe<Round> {
        return smartDao.getPendingRound(userId, levelId)
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

    override fun getListLevelsAndGroups(): Flowable<List<LevelModel>> {
        return smartDao.getListLevelsAndGroups()
    }

    override fun getCountLevels(): Flowable<Int> {
        return smartDao.getCountLevels()
    }

    override fun getLevelParams(levelId: Int): Single<LevelParams> {
        return smartDao.getLevelParams(levelId)
    }

}