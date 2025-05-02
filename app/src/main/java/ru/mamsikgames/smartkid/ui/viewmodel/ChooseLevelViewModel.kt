package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.domain.model.LevelGroupModel
import ru.mamsikgames.smartkid.domain.model.LevelModel

class ChooseLevelViewModel(private val chooseLevelInteractor: ChooseLevelInteractor) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _currentUserId = MutableLiveData<Int>()
    var currentUserId: LiveData<Int> = _currentUserId

    private val _listLevels = MutableLiveData<List<LevelModel>>()
    var listLevels: LiveData<List<LevelModel>> = _listLevels

    private val _listLevelGroups = MutableLiveData<List<LevelGroupModel>>()
    var listLevelGroups: LiveData<List<LevelGroupModel>> = _listLevelGroups

    private val _mapGroupLevels = MutableLiveData<Map<Int, Int>>()
    var mapGroupLevels: LiveData<Map<Int, Int>> = _mapGroupLevels

    init {
        getCurrentUser()
        getListLevelsAndGroups()
    }

    fun getCurrentUser() {
        chooseLevelInteractor.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _currentUserId.postValue(it.userId)
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
                _mapGroupLevels.postValue(getLevelGroupsMap(it))
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

    fun getLevelGroupsMap(levels: List<LevelModel>): Map<Int, Int> {

        val map = mutableMapOf<Int, Int>()
        var groupInd = 0

        map.put(0, groupInd)

        for (levelInd in levels.indices)
            if (levelInd > 0)
                if (levels[levelInd].groupName != levels[levelInd - 1].groupName)
                    map.put(levelInd, ++groupInd)

        return map
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}