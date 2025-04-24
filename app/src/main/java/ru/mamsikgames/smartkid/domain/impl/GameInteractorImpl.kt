package ru.mamsikgames.smartkid.domain.impl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor

class GameInteractorImpl(private val smartRepository: SmartRepositoryImpl) : GameInteractor {
    override fun getCurrentUser(): Flowable<UserEntity> {
        val user = smartRepository.getCurrentUser()
        return user
    }

    override fun insertRound(r: RoundEntity): Single<Long> {
        return smartRepository.insertRound(r)
    }

    override fun updateRound(r: RoundEntity): Completable {
        return smartRepository.updateRound(r)
    }

    override fun getPendingRound(
        userId: Int, levelId: Int
    ): Maybe<RoundEntity> {
        return smartRepository.getPendingRound(userId, levelId)
    }

}