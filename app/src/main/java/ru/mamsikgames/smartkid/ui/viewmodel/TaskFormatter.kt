package ru.mamsikgames.smartkid.ui.viewmodel

import ru.mamsikgames.smartkid.domain.model.Task

object TaskFormatter {
    private val operationsMap = mapOf(
        "plus" to "+",
        "minus" to "-",
        "multiply" to "×",
        "divide" to ":"
    )

    fun format(task: Task, input: Int?): TaskRenderParams {
        val inputStr = input?.toString() ?: " "
        val operatorSymbol = operationsMap[task.operation] ?: ""

        val taskStr = when (task.equationType) {
            1 -> "$inputStr $operatorSymbol ${task.op2} = ${task.res}"
            2 -> "${task.op1} $operatorSymbol $inputStr = ${task.res}"
            else -> "${task.op1} $operatorSymbol ${task.op2} = $inputStr"
        }

        val taskParams = TaskRenderParams(taskStr)

        when (task.equationType) {
            1 -> {
                taskParams.spanStart = 0
                taskParams.spanEnd = input.toString().length
            }

            2 -> {
                taskParams.spanStart = task.op1.toString().length + 3
                taskParams.spanEnd = taskParams.spanStart + input.toString().length
            }
        }

        if (input != null) {
            taskParams.btnOkState = true
            taskParams.btnEraseState = false
        } else {
            taskParams.btnOkState = false
            taskParams.btnEraseState = true
        }

        return taskParams
    }

}