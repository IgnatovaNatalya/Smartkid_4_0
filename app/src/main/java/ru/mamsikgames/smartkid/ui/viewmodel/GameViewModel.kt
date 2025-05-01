package ru.mamsikgames.smartkid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
            .subscribe(
                { it ->
                    round = it
                    _roundRenderParams.postValue(round)
                }, { error ->
                    println("$error Error getting pending round")
                    round = createNewRound(userId, levelId)
                    _roundRenderParams.postValue(round)
                }, {
                    round = createNewRound(userId, levelId)
                    _roundRenderParams.postValue(round)
                }).let {
                compositeDisposable.add(it)
            }
    }


    private fun createNewRound(userId: Int, levelId: Int): Round {
        return Round(
            userId = userId,
            levelId = levelId,
            roundBegin = currentTimeMillis()
        )
    }

    fun pressNum(pressedNum: Int) {
        inputNum = inputNum?.let { it * 10 + pressedNum }
        if (inputNum == null) inputNum = pressedNum

        _taskRenderParams.postValue(TaskFormatter.format(task, inputNum))
    }

    fun pressErase() {
        inputNum = inputNum?.let {
            if (it>9) inputNum?.div(10)
            else null
        }
        _taskRenderParams.postValue((TaskFormatter.format(task, inputNum)))
    }

    fun pressOK() {
        inputNum?.let {
            val result = gameInteractor.validateAnswer(task, it)
            updateRoundInfo(result)
            if (result) newTask()
            inputNum = null
        }
    }

    private fun updateRoundInfo(result: Boolean) {
        if (result)
            round.numCorrect = round.numCorrect!! + 1
        else
            round.numWrong = round.numWrong!! + 1

        round.numTasks = round.numTasks!! + 1
        round.numEfforts = round.numEfforts!! + 1

        _roundRenderParams.postValue(round)
        _answerState.postValue(result)
    }

    fun newTask() {
        task = gameInteractor.newTask(level)
        _taskRenderParams.postValue(TaskFormatter.format(task, null))
    }

    fun completeRound() {
        saveRound(round)
    }

    private fun saveRound(round: Round) {
        gameInteractor.insertRound(Round.convertToDb(round))
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


