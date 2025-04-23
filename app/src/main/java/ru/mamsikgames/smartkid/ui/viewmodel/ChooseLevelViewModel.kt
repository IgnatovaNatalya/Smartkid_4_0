package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.OperationEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor

//class ChooseLevelViewModel(private val smartRepository: SmartRepository) : ViewModel()  {

class ChooseLevelViewModel(private val chooseLevelInteractor: ChooseLevelInteractor) : ViewModel()  {

    private val compositeDisposable = CompositeDisposable()

    private val _recordCurUser = MutableLiveData<UserEntity>()
    var recordCurUser: LiveData<UserEntity> = _recordCurUser

    private val _recordOperations = MutableLiveData<List<OperationEntity>>()
    var recordOperations:LiveData<List<OperationEntity>> = _recordOperations

    fun getCurrentUser() {
        chooseLevelInteractor.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordCurUser.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListOperations() {
        chooseLevelInteractor.getListOperations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordOperations.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }
    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}