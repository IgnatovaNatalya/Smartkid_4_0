package ru.mamsikgames.smartkid.domain.impl

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor

class ChooseLevelInteractorImpl(private val smartRepository: SmartRepositoryImpl) : ChooseLevelInteractor{
    override fun getCurrentUser(): Flowable<UserEntity> {
        val user = smartRepository.getCurrentUser()
        return user
    }

    override fun getListLevels(): Flowable<List<LevelEntity>> {
        val listOperations = smartRepository.getListLevels()
        return listOperations
    }

    override fun getListLevelGroups(): Flowable<List<LevelGroupEntity>> {
        return smartRepository.getListLevelGroups()
    }
}