package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.UserEntity
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.interactor.ChooseLevelInteractor
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor

class GameViewModel(private val gameInteractor: GameInteractor) : ViewModel()  {

    private val compositeDisposable = CompositeDisposable()

    private val _recordCurUser = MutableLiveData<UserEntity>()
    var recordCurUser: LiveData<UserEntity> = _recordCurUser

    private val _recordOperations = MutableLiveData<List<LevelEntity>>()
    var recordLevels:LiveData<List<LevelEntity>> = _recordOperations

    private val _recordNewRound = MutableLiveData<Long>()
    var recordNewRound: LiveData<Long> = _recordNewRound

    private val _recordPendingRound = MutableLiveData<RoundEntity>()
    var recordPendingRound: LiveData<RoundEntity> = _recordPendingRound

    fun getPendingRound(userId: Int, operationId: Int) {
        gameInteractor.getPendingRound(userId, operationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordPendingRound.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun insertRound(r: RoundEntity) {
        gameInteractor.insertRound(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recordNewRound.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun updateRound(r: RoundEntity) {
        gameInteractor.updateRound(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
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