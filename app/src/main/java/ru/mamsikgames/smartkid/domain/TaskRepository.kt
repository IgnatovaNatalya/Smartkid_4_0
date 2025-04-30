package ru.mamsikgames.smartkid.domain

import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Task

interface TaskRepository {
    fun getTask(level: LevelParams): Task
    fun generateNewTask(level: LevelParams): Task
    fun validateAnswer(): Boolean
}