package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Task

class GameViewModel(private val gameInteractor: GameInteractor) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _taskRenderParams = MutableLiveData<TaskRenderParams>()
    var taskRenderParams: LiveData<TaskRenderParams> = _taskRenderParams


    private val _newRoundId = MutableLiveData<Long>()
    var newRoundId: LiveData<Long> = _newRoundId

    private val _pendingRound = MutableLiveData<RoundEntity>()
    var pendingRound: LiveData<RoundEntity> = _pendingRound

    private var inputNum: Int? = null
    private lateinit var task: Task

    fun start(levelId:Int) {
        getTask(levelId)
    }

    fun getTask(levelId: Int) {
        gameInteractor.getLevelParams(levelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                task = gameInteractor.getTask(it)
                _taskRenderParams.postValue(TaskFormatter.format(task, null))
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun pressNum(pressedNum: Int) {
        inputNum = inputNum?.let { it * 10 + pressedNum }
        if (inputNum == null) inputNum = pressedNum

        _taskRenderParams.postValue(TaskFormatter.format(task, inputNum))
    }

    fun pressErase() {
        inputNum = inputNum?.div(10)
    }

    fun pressOk() {
        if (validateAnswer()) {

        }
        else {

        }
    }
    private fun validateAnswer() {
        if (thinkManager.testRes(inputNum)) {
            gameSounds.playSoundCor()
            gameSounds.playSoundCorrect()
            setCorrect(++round.numCorrect)
            newTask()
        } else {
            gameSounds.playSoundWrong()
            setWrong(++round.numWrong)
        }
        round.numEfforts++
        inputNum = 0

        updateRound()
        printTask(null)
        state = false
        setOkButtonState(state)
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