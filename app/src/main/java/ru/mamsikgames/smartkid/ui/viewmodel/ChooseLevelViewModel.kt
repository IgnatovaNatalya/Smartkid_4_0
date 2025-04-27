package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor

//class ChooseLevelViewModel(private val smartRepository: SmartRepository) : ViewModel()  {

class ChooseLevelViewModel(private val chooseLevelInteractor: ChooseLevelInteractor) : ViewModel()  {

    private val compositeDisposable = CompositeDisposable()

    private val _currentUser = MutableLiveData<UserEntity>()
    var currentUser: LiveData<UserEntity> = _currentUser

    private val _listLevels = MutableLiveData<List<LevelEntity>>()
    var listLevels:LiveData<List<LevelEntity>> = _listLevels

    private val _listLevelGroups = MutableLiveData<List<LevelGroupEntity>>()
    var listLevelGroups:LiveData<List<LevelGroupEntity>> = _listLevelGroups

    init {
        getListLevels()
        getListLevelGroups()
    }

    fun getCurrentUser() {
        chooseLevelInteractor.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _currentUser.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListLevels() {
        chooseLevelInteractor.getListLevels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listLevels.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListLevelGroups() {
        chooseLevelInteractor.getListLevelGroups()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listLevelGroups.postValue(it)
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