package ru.mamsikgames.smartkid.domain.model

data class User(
    var userId: Int = 0,
    var userName: String = "",
    var isCurrent: Boolean = false
)
