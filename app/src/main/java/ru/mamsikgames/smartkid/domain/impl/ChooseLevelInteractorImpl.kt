package ru.mamsikgames.smartkid.domain.impl

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.domain.model.LevelModel

class ChooseLevelInteractorImpl(private val smartRepository: SmartRepository) : ChooseLevelInteractor{
    override fun getCurrentUser(): Flowable<UserEntity> {
        val user = smartRepository.getCurrentUser()
        return user
    }

    override fun getListLevelsAndGroups(): Flowable<List<LevelModel>> {
        return smartRepository.getListLevelsAndGroups()
    }

}