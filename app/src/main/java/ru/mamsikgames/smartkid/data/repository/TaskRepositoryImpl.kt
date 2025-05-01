package ru.mamsikgames.smartkid.data.repository

import ru.mamsikgames.smartkid.domain.TaskRepository
import ru.mamsikgames.smartkid.domain.model.LevelParams
import ru.mamsikgames.smartkid.domain.model.Task
import kotlin.math.min

class TaskRepositoryImpl : TaskRepository {

    private val taskStorage = mutableMapOf<Int, Task>()

    val operations: Array<String> = arrayOf( //todo переделать к чертям на енумы
        "plus",
        "minus",
        "multiply",
        "divide"
    )

    override fun getTask(level: LevelParams): Task {
        val savedTask = taskStorage[level.id]
        return savedTask ?: generateNewTask(level)
    }

    override fun generateNewTask(level: LevelParams): Task {
        with(level) {
            if ((relationOp1 != null) && (resMin == null)) throw IllegalArgumentException("Wrong parameters")
            if ((relationOp2 != null) && (resMin == null)) throw IllegalArgumentException("Wrong parameters")
        }

        val task = when (level.operation) {
            "plus" -> generatePlusTask(level)
            "minus" -> generateMinusTask(level)
            "multiply" -> generateMultiplyTask(level)
            "divide" -> generateDivideTask(level)
            else -> throw IllegalArgumentException("Unknown operation")
        }

        taskStorage[level.id] = task

        return task
    }

    override fun validateAnswer(task: Task, input: Int): Boolean {
        with(task) {
            when (task.equationType) {
                null ->
                    when (operation) {
                        operations[0] -> return op1 + op2 == input
                        operations[1] -> return op1 - op2 == input
                        operations[2] -> return op1 * op2 == input
                        operations[3] -> return op1 / op2 == input
                    }

                1 -> when (operation) {
                    operations[0] -> return input + op2 == res
                    operations[1] -> return input - op2 == res
                }

                2 -> when (operation) {
                    operations[0] -> return op1 + input == res
                    operations[1] -> return op1 - input == res
                }
            }
            return false
        }
    }

    private fun generatePlusTask(level: LevelParams): Task {
        var op1 = 0
        var op2 = 0
        var res = 0

        var oop1: Int? = null
        var oop2: Int? = null
        var rres: Int? = null

        with(level) {
            if ((op1Min != null && op1Max == null) &&
                (op2Min != null && op2Max == null) &&
                (resMin == null && resMax != null)
            ) {
                oop1 = ((op1Min + op2Min)..(resMax - op2Min)).random()
                rres = ((oop1 + op2Min)..resMax).random()
                oop2 = rres - oop1

            } else
                if (resMin != null) {
                    rres = (resMin..resMax!!).random()

                    if (op1Min != null) oop1 =
                        if (relationOp1 != null) (rres - relationOp1..op1Max!!).random() else (op1Min..rres).random()
                    if (op2Min != null) oop2 =
                        if (relationOp2 != null) (rres - relationOp2..op2Max!!).random() else (op2Min..rres).random()

                } else {
                    if (op1Min != null) oop1 = (op1Min..op1Max!!).random()
                    if (op2Min != null) oop2 = (op2Min..op2Max!!).random()
                }

            if ((oop1 != null) && (oop2 != null)) rres = oop1 + oop2
            if ((oop1 != null) && (rres != null)) oop2 = rres - oop1
            if ((oop2 != null) && (rres != null)) oop1 = rres - oop2

            oop1?.let { op1 = it }
            oop2?.let { op2 = it }
            rres?.let { res = it }

            return Task(op1, op2, res, operation, equation)
        }
    }



    private fun generateMinusTask(level: LevelParams): Task {
        var op1 = 0
        var op2 = 0
        var res = 0

        var oop1: Int? = null
        var oop2: Int? = null
        var rres: Int? = null

        with(level) {
            if ((op1Min != null) && (op1Max != null)) oop1 = (op1Min..op1Max).random()
            if ((op2Min != null) && (op2Max != null))
                oop2 = if (oop1 != null)
                    (op2Min until min(oop1, op2Max)).random()
                else
                    (op2Min..op2Max).random()
            if ((resMin != null) && (resMax != null)) rres = (resMin..resMax).random()

            if ((oop1 != null) && (oop2 != null)) rres = oop1 - oop2
            if ((oop1 != null) && (rres != null)) oop2 = oop1 - rres
            if ((oop2 != null) && (rres != null)) oop1 = rres + oop2

            if (oop1 != null) op1 = oop1
            if (oop2 != null) op2 = oop2
            if (rres != null) res = rres

            return Task(op1, op2, res, operation, equation)
        }
    }

    private fun generateMultiplyTask(level: LevelParams): Task {

        var op1 = 0
        var op2 = 0
        var res = 0

        var oop1: Int? = null
        var oop2: Int? = null
        var rres: Int? = null

        with(level) {
            if ((op1Min != null) && (op1Max != null)) oop1 = (op1Min..op1Max).random()
            if ((op2Min != null) && (op2Max != null)) oop2 = (op2Min..op2Max).random()
            if ((resMin != null) && (resMax != null)) rres = (resMin..resMax).random()

            if ((oop1 != null) && (oop2 != null)) rres = oop1 * oop2
            if ((oop1 != null) && (rres != null)) oop2 = rres / oop1
            if ((oop2 != null) && (rres != null)) oop1 = rres / oop2

            if (oop1 != null) op1 = oop1
            if (oop2 != null) op2 = oop2
            if (rres != null) res = rres

            return Task(op1, op2, res, operation, equation)
        }
    }

    private fun generateDivideTask(level: LevelParams): Task {

        with(level) {
            if ((op1Min == null) || (op1Max == null) || (op2Min == null) || (op2Max == null))
                throw IllegalArgumentException("Wrong parameters")

            val op1 = (op1Min..op1Max).random()
            val op2 = (op2Min..op2Max).random()

            val res = op1 * op2

            return Task(op1, op2, res, operation, equation)
        }
    }
}

