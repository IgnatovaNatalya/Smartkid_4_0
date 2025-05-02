package ru.mamsikgames.smartkid.ui.viewmodel

data class TaskRenderParams(
    var taskStr: String,
    var spanStart: Int = -1,
    var spanEnd: Int = -1,
    var btnEnterState:Boolean = false,
    var btnEraseState:Boolean = false
)
