package ru.mamsikgames.smartkid.domain.impl

import io.reactivex.rxjava3.core.Flowable
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import ru.mamsikgames.smartkid.data.repository.SmartRepositoryImpl
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.data.MapUtils.*

class ChooseLevelInteractorImpl(private val smartRepository: SmartRepositoryImpl) : ChooseLevelInteractor{
    override fun getCurrentUser(): Flowable<UserEntity> {
        val user = smartRepository.getCurrentUser()
        return user
    }

    override fun getListOperations(): Flowable<List<OperationEntity>> {
        val listOperations = smartRepository.getListOperations()
        return listOperations
    }


}