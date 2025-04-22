package ru.mamsikgames.smartkid.core

import kotlin.math.min
import ru.mamsikgames.smartkid.data.db.entity.Operation

class Task (_op1:Int, _op2:Int, _op3:Int, _op:String, _eq:Int?) {
    var op1 = _op1
    var op2 = _op2
    var op3 = _op3
    var op=_op
    var eq=_eq
}

object ThinkManager {

    var op1:Int = 0
    private var op2:Int = 0
    private var opStr:String =""

    var res:Int =0

    private val operations: Array<String> = arrayOf(
        "plus",
        "minus",
        "multiply",
        "divide")

    private var op = operations[0]
    private var eq:Int? = null

    private val operationsStr = arrayOf(
        "+","-","×",":"
    )

    private var taskStorage = mutableMapOf<Int,Task>()

    fun newTask(oper:Operation):String {
        //ограничения по оператору , если они есть, должны быть оба заданы. По сумме тоже
        //если не заданы ограничения, то тоже оба
        //обязательно должно быть задно ограничение по двум из трех параметров задачи
        //relation может быть задан только с заданным constraintRes

        val cResMin = oper.resMin
        val cOp1Min = oper.op1Min
        val cOp2Min = oper.op2Min
        val cOp1Max = oper.op1Max
        val cOp2Max = oper.op2Max
        val cResMax = oper.resMax
        val rOp1 = oper.relationOp1
        val rOp2 = oper.relationOp2

        if (oper.operation !in operations) throw IllegalArgumentException(
            "${oper.codeName} :Параметр operation должен быть одним из $operations, operation = ${oper.operation}")

        //if ( (cOp1Max == null) xor (cOp1Min == null)) throw IllegalArgumentException(
        //    "${oper.operationLevel} : Должны быть заданы оба ограничения на аргумент Op1: constraintOp1Min,  constraintOp1Max")
        //if ( (cOp2Max == null) xor (cOp2Min == null)) throw IllegalArgumentException(
        //    "${oper.operationLevel} : Должны быть заданы оба ограничения на аргумент Op2: constraintOp2Min,  constraintOp2Max")
        //if ( (cResMax == null) xor (cResMin == null)) throw IllegalArgumentException(
        //    "${oper.operationLevel} : Должны быть заданы оба ограничения на аргумент Res: constraintResMin,  constraintResMax")

        //if ((cResMax== null) && (cOp1Max == null) && (cOp2Max == null)) throw IllegalArgumentException(
        //    "${oper.operationLevel} : Должны быть ограничены хотя бы два из трех параметров задачи: constraintOp1,  constraintOp2 , constraintRes")

        //if ((cResMax!= null) && (cOp1Max != null) && (cOp2Max != null)) throw IllegalArgumentException(
        //    "${oper.operationLevel} : Не могут быть ограничены все три параметра задачи: constraintOp1,  constraintOp2 , constraintRes")

        if ((rOp1!=null) && (cResMin==null) ) throw IllegalArgumentException(
            "${oper.codeName} : Параметр relationOp1 может быть задан только вместе с cResMin и cResMax")

        if ((rOp2!=null) && (cResMin==null) ) throw IllegalArgumentException(
            "${oper.codeName} : Параметр relationOp2 может быть задан только вместе с cResMin и cResMax")

        op = oper.operation
        eq = oper.equation

        var oop1:Int? = null
        var oop2:Int? = null
        var rres:Int? = null

        //if ((cOp1Min != null) && (cOp1Max!=null))   oop1= (cOp1Min..cOp1Max).random()
        //if ((cOp2Min != null) && (cOp2Max!=null))   oop2= (cOp2Min..cOp2Max).random()
        //if ((cResMin != null) && (cResMax!=null))   rres= (cResMin..cResMax).random()

        when (oper.operation) {

            operations[0] -> { //plus
                if (( cOp1Min != null && cOp1Max == null ) &&
                    ( cOp2Min != null && cOp2Max == null ) &&
                    ( cResMin == null && cResMax != null ))
                {
                    rres = (cOp1Min..cResMax).random()
                    oop1 = (cOp1Min..rres).random()
                    oop2 = rres - oop1

                }
                else
                    if (cResMin != null)  {
                        rres= (cResMin..cResMax!!).random()

                        if (cOp1Min != null) oop1 = if (rOp1!=null) (rres - rOp1..cOp1Max!!).random() else (cOp1Min..rres).random()
                        if (cOp2Min != null) oop2 = if (rOp2!=null) (rres - rOp2..cOp2Max!!).random() else (cOp2Min..rres).random()

                    }
                    else {
                        if (cOp1Min != null) oop1 = (cOp1Min..cOp1Max!!).random()
                        if (cOp2Min != null) oop2 = (cOp2Min..cOp2Max!!).random()
                    }


                if ((oop1!= null)&&(oop2!=null)) rres = oop1 + oop2
                if ((oop1!= null)&&(rres!=null)) oop2 = rres - oop1
                if ((oop2!= null)&&(rres!=null)) oop1 = rres - oop2

            }
            operations[1] ->  { //minus
                if ((cOp1Min != null) && (cOp1Max!=null))   oop1= (cOp1Min..cOp1Max).random()
                if ((cOp2Min != null) && (cOp2Max!=null))
                    oop2 = if (oop1!=null)
                        (cOp2Min until min(oop1,cOp2Max)).random()
                    else
                        (cOp2Min..cOp2Max).random()
                if ((cResMin != null) && (cResMax!=null))   rres= (cResMin..cResMax).random()

                if ((oop1!= null)&&(oop2!=null)) rres  = oop1 - oop2
                if ((oop1!= null)&&(rres!=null)) oop2 = oop1 - rres
                if ((oop2!= null)&&(rres!=null)) oop1 = rres + oop2
            }

            operations[2] -> {//multiply
                if ((cOp1Min != null) && (cOp1Max!=null))   oop1= (cOp1Min..cOp1Max).random()
                if ((cOp2Min != null) && (cOp2Max!=null))   oop2= (cOp2Min..cOp2Max).random()
                if ((cResMin != null) && (cResMax!=null))   rres= (cResMin..cResMax).random()

                if ((oop1!= null)&&(oop2!=null)) rres  = oop1 * oop2
                if ((oop1!= null)&&(rres!=null)) oop2 = rres / oop1
                if ((oop2!= null)&&(rres!=null)) oop1 = rres / oop2
            }

            operations[3] -> {//divide
                if ((cOp1Min != null) && (cOp1Max!=null))   oop1= (cOp1Min..cOp1Max).random()
                if ((cOp2Min != null) && (cOp2Max!=null))   oop2= (cOp2Min..cOp2Max).random()
                if ((cResMin != null) && (cResMax!=null))   rres= (cResMin..cResMax).random()

                if ((oop1!= null)&&(oop2!=null)) rres  = oop1 / oop2
                if ((oop1!= null)&&(rres!=null)) oop2 = oop1 / rres
                if ((oop2!= null)&&(rres!=null)) oop1 = rres * oop2
            }

        }
        if (oop1 != null)  op1=oop1
        if (oop2 != null)  op2=oop2
        if (rres != null)  res=rres

        if (oper.id!=null) taskStorage[oper.id]=Task(op1,op2,res,op,eq)
        return showTask()
    }




    fun getTask(operationId:Int):Boolean {

        val oldTask = taskStorage[operationId]

         if (oldTask != null) {
             op1 = oldTask.op1
             op2 = oldTask.op2
             res = oldTask.op3
             op = oldTask.op
             eq = oldTask.eq
             return true
         }
        return false
    }

    fun printTask():String {
        val n = operations.indexOf(op)
        opStr = operationsStr[n]

        var str = ""

        if (eq != null) {
            when (eq) {
                1 -> str = "   $opStr $op2 = $res"
                2 -> str = "$op1 $opStr   = $res"
            }
        }
        else {
            str = "$op1 $opStr $op2 ="
        }
        return str
    }

    fun printTask(withInput:Int):String {
        val n = operations.indexOf(op)
        opStr = operationsStr[n]

        var str = ""

        if (eq != null) {
            when (eq) {
                1 -> str = "$withInput $opStr $op2 = $res"
                2 -> str = "$op1 $opStr $withInput = $res"
            }
        }
        else {
            str = "$op1 $opStr $op2 = $withInput"
        }
        return str
    }

    private fun showTask():String {
        val n = operations.indexOf(op)
        opStr = operationsStr[n]

        return  "$op1 $opStr $op2 = $res"
    }

    fun testRes(testNum:Int) :Boolean {
        when (eq) {
            null ->
                when (op) {
                    operations[0] -> return op1 + op2 == testNum
                    operations[1] -> return op1 - op2 == testNum
                    operations[2] -> return op1 * op2 == testNum
                    operations[3] -> return op1 / op2 == testNum
                }
            1 -> when (op) {
                    operations[0] -> return testNum + op2 == res
                    operations[1] -> return testNum - op2 == res
                }

            2 -> when (op) {
                operations[0] -> return op1 + testNum == res
                operations[1] -> return op1 - testNum == res
                }
        }

        return false
    }
 }