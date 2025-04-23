package ru.mamsikgames.smartkid.domain.interactor

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity

interface ChooseLevelInteractor {
    fun getCurrentUser(): Flowable<UserEntity>
    fun getListOperations(): Flowable<List<OperationEntity>>
}