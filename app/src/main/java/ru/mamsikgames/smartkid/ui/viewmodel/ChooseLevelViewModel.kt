package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.UserEntity
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.domain.model.LevelGroupModel
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.ui.util.LevelGroupAdaptersAssociator

class ChooseLevelViewModel(private val chooseLevelInteractor: ChooseLevelInteractor) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _currentUser = MutableLiveData<UserEntity>()
    var currentUser: LiveData<UserEntity> = _currentUser

    private val _listLevels = MutableLiveData<List<LevelModel>>()
    var listLevels: LiveData<List<LevelModel>> = _listLevels

    private val _listLevelGroups = MutableLiveData<List<LevelGroupModel>>()
    var listLevelGroups: LiveData<List<LevelGroupModel>> = _listLevelGroups

    private val _listLevelGroupAdaptersAssociator = MutableLiveData<List<LevelGroupAdaptersAssociator>>()
    var listLevelGroupAdaptersAssociator: LiveData<List<LevelGroupAdaptersAssociator>> = _listLevelGroupAdaptersAssociator

    init {
        getListLevelsAndGroups()
        //getListLevelGroups()
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

    fun getListLevelsAndGroups() {
        chooseLevelInteractor.getListLevelsAndGroups()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listLevels.postValue(it)
                _listLevelGroups.postValue(getListLevelGroups(it))
                _listLevelGroupAdaptersAssociator.postValue(getLevelGroupsAssociator(it))
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun getListLevelGroups(levels: List<LevelModel>): List<LevelGroupModel> {
        val list = mutableListOf<LevelGroupModel>()
        list.add(LevelGroupModel(groupName = levels[0].groupName))
        for (pos in levels.indices) {
            if (pos > 0)
                if (levels[pos].groupName != levels[pos - 1].groupName) list.add(
                    LevelGroupModel(levels[pos].groupName)
                )
        }
        return list
    }
    fun getLevelGroupsAssociator(levels: List<LevelModel>): List<LevelGroupAdaptersAssociator> {

        val list = mutableListOf<LevelGroupAdaptersAssociator>()
        var groupInd = 0
        list.add(LevelGroupAdaptersAssociator(levelAdapterPos = 0, groupAdapterPos = groupInd))

        for (levelInd in levels.indices) {
            if (levelInd > 0)
                if (levels[levelInd].groupName != levels[levelInd - 1].groupName) list.add(
                    LevelGroupAdaptersAssociator(
                        levelAdapterPos = levelInd,
                        groupAdapterPos = ++groupInd
                    )
                )
        }
        return list
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}