package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Round
import ru.mamsikgames.smartkid.domain.model.Task
import java.lang.System.currentTimeMillis

class GameViewModel(private val gameInteractor: GameInteractor) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _taskRenderParams = MutableLiveData<TaskRenderParams>()
    var taskRenderParams: LiveData<TaskRenderParams> = _taskRenderParams

    private val _answerState = MutableLiveData<Boolean>()
    var answerState: LiveData<Boolean> = _answerState

    private val _roundRenderParams = MutableLiveData<Round>()
    var roundRenderParams : LiveData<Round> = _roundRenderParams

    private lateinit var task: Task
    private lateinit var level: LevelParams
    private lateinit var round: Round

    private var inputNum: Int? = null

    fun start(userId: Int, levelId: Int) {
        getTask(levelId)
        getRound(userId, levelId)
    }

    private fun getTask(levelId: Int) {
        gameInteractor.getLevelParams(levelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                level = it
                task = gameInteractor.getTask(it)
                _taskRenderParams.postValue(TaskFormatter.format(task, null))
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun getRound(userId:Int, levelId:Int) {
        gameInteractor.getPendingRound(userId, levelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                round = it
                    ?: Round(
                        userId = userId,
                        levelId = levelId,
                        roundBegin = currentTimeMillis()
                    )
                _roundRenderParams.postValue(round)
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

    fun pressOK() {
        inputNum?.let {
            val result = gameInteractor.validateAnswer(task, it)

            if (result)
                round.numCorrect = round.numCorrect!! + 1
            else
                round.numWrong = round.numWrong!! + 1

            _roundRenderParams.postValue(round)

            round.numTasks = round.numTasks!! + 1
            round.numEfforts = round.numEfforts!! + 1

            _answerState.postValue(result)

            if (result) task = gameInteractor.newTask(level)
            _taskRenderParams.postValue(TaskFormatter.format(task, null))

            inputNum = null
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