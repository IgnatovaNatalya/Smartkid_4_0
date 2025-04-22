package ru.mamsikgames.smartkid.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Operation(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val name: String = "",
    val codeName: String = "",
    val op1Min: Int? = null,
    val op1Max: Int? = null,
    val op2Min: Int? = null,
    val op2Max: Int? = null,
    val resMin: Int? = null,
    val resMax: Int? = null,
    val operation: String = "",
    val equation: Int? = null,
    val relationOp1: Int? = null,
    val relationOp2: Int? = null,
    val ord: Int? = null,
    val groupName: String = "",

    ) : Serializable
