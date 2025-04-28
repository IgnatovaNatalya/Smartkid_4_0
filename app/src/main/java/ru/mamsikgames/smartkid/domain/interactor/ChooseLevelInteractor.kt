package ru.mamsikgames.smartkid.domain.interactor

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.domain.model.LevelModel

interface ChooseLevelInteractor {
    fun getCurrentUser(): Flowable<UserEntity>
    fun getListLevelsAndGroups(): Flowable<List<LevelModel>>
}