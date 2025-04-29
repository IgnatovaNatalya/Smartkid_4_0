package ru.mamsikgames.smartkid.ui.viewmodel

import android.content.AsyncTaskLoader
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.mamsikgames.smartkid.core.Task
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor
import ru.mamsikgames.smartkid.domain.model.LevelParams

class GameViewModel(private val gameInteractor: GameInteractor) : ViewModel()  {

    private val compositeDisposable = CompositeDisposable()


    private val _levelParams = MutableLiveData<LevelParams>()
    var levelParams:LiveData<LevelParams> = _levelParams

    private val _task = MutableLiveData<Task>()
    var task : LiveData<Task> = _task

    private val _newRoundId = MutableLiveData<Long>()
    var newRoundId: LiveData<Long> = _newRoundId

    private val _pendingRound = MutableLiveData<RoundEntity>()
    var pendingRound: LiveData<RoundEntity> = _pendingRound

    fun getLevelParams(levelId:Int) {
        gameInteractor.getLevelParams(levelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _levelParams.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }
    fun newTask(level:LevelParams) {
        gameInteractor.newTask(level)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _task.postValue(it)
            }, {
            }).let {
                compositeDisposable.add(it)
            }

    }

    fun getPendingRound(userId: Int, operationId: Int) {
        gameInteractor.getPendingRound(userId, operationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _pendingRound.postValue(it)
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
                _newRoundId.postValue(it)
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
        super.onCleared()
    }
}