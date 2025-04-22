package ru.mamsikgames.smartkid.ui.activity

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.core.ThinkManager
import ru.mamsikgames.smartkid.data.db.entity.Operation
import ru.mamsikgames.smartkid.data.db.entity.Round
import ru.mamsikgames.smartkid.ui.viewmodel.SmartViewModel
import java.lang.System.currentTimeMillis
import ru.mamsikgames.smartkid.R


class GameActivity : AppCompatActivity() {
    private var inputNum: Int =0

    private lateinit var thinkManager: ThinkManager
    private lateinit var operation: Operation

    private var gameSounds = GameSounds

    private var currentUserId = 1
    private var currentUserName = ""
    private  var operId:Int =0

    private var state = false

    private var round = Round(
        null,
        currentUserId,
        0,
        currentTimeMillis() ,
        0L,
        false,
        0L,
        0,
        0,
        0,
        0
    )

    private lateinit var viewModel:SmartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar?.hide() ///

        if (!intent.hasExtra(EXTRA_OPERATION)) finish()
        operation = intent.extras?.get(EXTRA_OPERATION) as Operation
        if (operation.id != null) operId = operation.id!!

        currentUserId = intent.getIntExtra(EXTRA_USER_ID,0)
        currentUserName = intent.getStringExtra(EXTRA_USER_NAME).toString()

        round.operationId = operId
        round.userId = currentUserId

        thinkManager = ThinkManager
        viewModel = ViewModelProvider(this)[SmartViewModel::class.java]

        val tvUserName = findViewById<TextView>(R.id.textView_Game_userName)
        tvUserName.text = currentUserName

        val tvLevelName = findViewById<TextView>(R.id.textView_Game_levelName)
        tvLevelName.text = operation.codeName

        setListeners()


        val tvRound = findViewById<TextView>(R.id.textView_Game_Round)


    //get pending round
        viewModel.getPendingRound(currentUserId,operId)
        viewModel.recordPendingRound.observe(this) {
            if (it!=null) {
                round =  it
                tvRound.text = it.id.toString()
                setCorrect(round.numCorrect)
                setWrong(round.numWrong)
            }
        }

    //if created new round get id
        viewModel.recordNewRound.observe(this) {
            if (it!=null) round.id =  it.toLong()
            tvRound.text = it.toString()
        }

    //create & print task
        if (!thinkManager.getTask(operId))
            newTask()

        printTask(null)
        setOkState(state)

    }

    private fun finishAndSave() {
        val dialogFinishRound = DialogFinishRound(this, R.style.dialog_style)

        dialogFinishRound.onClickButton = {
            gameSounds.playSoundPlay()
            round.roundEnd = currentTimeMillis()
            round.duration = round.roundEnd - round.roundBegin

            round.finished = true
            this.updateRound()
            dialogFinishRound.dismiss()
            showResult()
        }

        dialogFinishRound.onClickCancel ={
            gameSounds.playSoundErase()
            dialogFinishRound.dismiss()
        }

        gameSounds.playSoundRestart()
        dialogFinishRound.show()
    }

    private fun showResult() {
        var place:Int

        viewModel.getRates(currentUserId)

        viewModel.recordRates.observe(this) { rates ->
            if (rates != null) {
                val myResult = rates.find { it.myPlace }!!
                place = rates.indexOf(myResult) +1

                val dialogRoundResults = DialogRoundResults(this, R.style.dialog_style,round,operation.codeName,place)

                dialogRoundResults.onClickButton = {
                    gameSounds.playSoundPlay()
                    dialogRoundResults.dismiss()
                    finish()
                }

                dialogRoundResults.onClickCancel = {
                    gameSounds.playSoundErase()
                    dialogRoundResults.dismiss()
                    finish()
                }

                gameSounds.playSoundRestart()
                dialogRoundResults.show()
            }}

        //gameSounds.playSoundRestart()
    }

    private fun newTask() {
        //val str =
        thinkManager.newTask(Operation(
            operation.id,
            operation.name,
            operation.codeName,
            operation.op1Min,
            operation.op1Max,
            operation.op2Min,
            operation.op2Max,
            operation.resMin,
            operation.resMax,
            operation.operation,
            operation.equation,
            operation.relationOp1,
            operation.relationOp2))

        round.numTasks++
    }

    /*private fun clearRound() {
        with(round) {

            this.id = null
            this.roundBegin= currentTimeMillis()
            this.roundEnd=0L
            this.finished = false
            this.numTasks = 0
            this.numEfforts = 0
            this.numCorrect = 0
            this.numWrong = 0
            this.numExits = 0
        }
        updateRound()
        setCorrect(0)
        setWrong(0)
    }*/

    private fun updateRound() {
        if (round.id !=null)
            viewModel.updateRound(round)
        else
            if (round.numTasks >0) viewModel.insertRound(round)
    }

    private fun testRes(res:Int) {
        if (!state) return

        if (thinkManager.testRes(res)) {
           gameSounds.playSoundCor()
           gameSounds.playSoundCorrect()
           setCorrect(++round.numCorrect)
           newTask()
        }
        else {
            gameSounds.playSoundWrong()
            setWrong(++round.numWrong)
        }
        round.numEfforts++
        inputNum = 0

        updateRound()
        printTask(null)
        state=false
        setOkState(state)
    }

    private fun pressNum(pressedNum: Int) {

        gameSounds.playSoundButton()

        if (inputNum <= 999999) {
            inputNum = if (inputNum>0) {
                inputNum*10 + pressedNum
            } else pressedNum
        }
        printTask(inputNum)
        state=true
        setOkState(state)
    }

    private fun printTask(withInput:Int?) {
        val tvTask = findViewById<TextView>(R.id.textfield_task)

        val str = if (withInput!=null) thinkManager.printTask(withInput) else thinkManager.printTask()

        when (operation.equation) {
            2 -> {
                val startSpan = if (thinkManager.op1> 9 ) 5 else 4
                val lenSpan = if (inputNum>9) 2 else 1
                val string = SpannableString(str)
                string.setSpan(UnderlineSpan(), startSpan, startSpan+lenSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvTask.text = string
            }
            1 -> { ///
                val startSpan = 0
                val lenSpan = if (inputNum>9) 2 else 1
                val string = SpannableString(str)
                string.setSpan(UnderlineSpan(), startSpan, startSpan+lenSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvTask.text = string
            }
            null ->
                tvTask.text = str
        }
    }

    private fun setCorrect(cor: Int) {
        val corTextView = findViewById<View>(R.id.correct_counter) as TextView
        corTextView.text = cor.toString()
    }

    private fun setWrong(wr: Int) {
        val wrTextView = findViewById<View>(R.id.wrong_counter) as TextView
        wrTextView.text = wr.toString()
    }

    private fun doExit() {
        round.numExits++
        updateRound()
        gameSounds.playSoundExit()
    }

    override fun onPause() {
        doExit()
        super.onPause()
    }

    private fun setOkState(state: Boolean) {
        val ivBtnOk = findViewById<ImageView>(R.id.button_ok)
        if (state)
            ivBtnOk.setImageResource(R.drawable.btn_ok)
        else
            ivBtnOk.setImageResource(R.drawable.btn_ok_dis)
    }

    private fun setListeners() {
        val ivBtnOk = findViewById<ImageView>(R.id.button_ok)
        ivBtnOk.setOnClickListener{ testRes(inputNum) }

        val ivBtnErase = findViewById<ImageView>(R.id.button_erase)
        ivBtnErase.setOnClickListener{
            gameSounds.playSoundErase()
            inputNum /= 10
            if (inputNum ==0) {
                printTask(null)
                state = false
                setOkState(state)
            }
            else {
                printTask(inputNum)
                state = true
                setOkState(state)
            }
        }

        val ivBtnBack = findViewById<ImageView>(R.id.button_back)
        ivBtnBack.setOnClickListener { finish() }

        val tvCounter:TextView = findViewById(R.id.correct_counter)
        tvCounter.setOnClickListener { finishAndSave() }

        val ivBtn1 = findViewById<ImageView>(R.id.button01)
        val ivBtn2 = findViewById<ImageView>(R.id.button02)
        val ivBtn3 = findViewById<ImageView>(R.id.button03)
        val ivBtn4 = findViewById<ImageView>(R.id.button04)
        val ivBtn5 = findViewById<ImageView>(R.id.button05)
        val ivBtn6 = findViewById<ImageView>(R.id.button06)
        val ivBtn7 = findViewById<ImageView>(R.id.button07)
        val ivBtn8 = findViewById<ImageView>(R.id.button08)
        val ivBtn9 = findViewById<ImageView>(R.id.button09)
        val ivBtn0 = findViewById<ImageView>(R.id.button00)

        ivBtn1.setOnClickListener { pressNum(1) }
        ivBtn2.setOnClickListener { pressNum(2) }
        ivBtn3.setOnClickListener { pressNum(3) }
        ivBtn4.setOnClickListener { pressNum(4) }
        ivBtn5.setOnClickListener { pressNum(5) }
        ivBtn6.setOnClickListener { pressNum(6) }
        ivBtn7.setOnClickListener { pressNum(7) }
        ivBtn8.setOnClickListener { pressNum(8) }
        ivBtn9.setOnClickListener { pressNum(9) }
        ivBtn0.setOnClickListener { pressNum(0) }
    }
}