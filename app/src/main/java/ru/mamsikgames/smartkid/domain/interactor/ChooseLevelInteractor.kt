package ru.mamsikgames.smartkid.domain.interactor

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity

interface ChooseLevelInteractor {
    fun getCurrentUser(): Flowable<UserEntity>
    fun getListLevels(): Flowable<List<LevelEntity>>
    fun getListLevelGroups(): Flowable<List<LevelGroupEntity>>
}