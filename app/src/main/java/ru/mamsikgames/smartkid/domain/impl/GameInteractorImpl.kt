package ru.mamsikgames.smartkid.domain.impl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.domain.TaskRepository
import ru.mamsikgames.smartkid.domain.interactor.GameInteractor
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Round
import ru.mamsikgames.smartkid.domain.model.Task

class GameInteractorImpl (
    private val smartRepository: SmartRepository,
    private val taskRepository: TaskRepository,
) : GameInteractor {

    override fun getLevelParams(id: Int): Single<LevelParams> {
        return smartRepository.getLevelParams(id)
    }

    override fun insertRound(r: RoundEntity): Single<Long> {
        return smartRepository.insertRound(r)
    }

    override fun updateRound(r: RoundEntity): Completable {
        return smartRepository.updateRound(r)
    }

    override fun getPendingRound(userId: Int, levelId: Int): Maybe<Round> {
        return smartRepository.getPendingRound(userId, levelId)
    }

    override fun getTask(levelParams: LevelParams): Task {
        return taskRepository.getTask(levelParams)
    }
    override fun newTask(levelParams: LevelParams): Task {
        return taskRepository.generateNewTask(levelParams)
    }

    override fun validateAnswer( task: Task, input: Int ): Boolean {
        return taskRepository.validateAnswer(task, input)
    }

}