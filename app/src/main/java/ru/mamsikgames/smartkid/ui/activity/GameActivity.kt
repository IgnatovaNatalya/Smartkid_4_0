package ru.mamsikgames.smartkid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.core.ThinkManager
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import java.lang.System.currentTimeMillis
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.databinding.ActivityGameBinding
import ru.mamsikgames.smartkid.ui.viewmodel.GameViewModel
import kotlin.getValue


class GameActivity : AppCompatActivity() {
    private var inputNum: Int =0

    private val thinkManager: ThinkManager by inject()
    private val gameSounds:  GameSounds  by inject()
    private val viewModel: GameViewModel  by viewModel()

    private lateinit var binding: ActivityGameBinding

    private lateinit var level: LevelEntity

    private var currentUserId = 1
    private var currentUserName = ""
    private  var levelId:Int =0

    private var state = false

    private var round = RoundEntity(
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() ///

        if (!intent.hasExtra(EXTRA_LEVEL)) finish()
        level = intent.extras?.get(EXTRA_LEVEL) as LevelEntity
        if (level.id != null) levelId = level.id!!

        currentUserId = intent.getIntExtra(EXTRA_USER_ID,0)

        round.levelId = levelId
        round.userId = currentUserId

        setListeners()

        val tvRound = findViewById<TextView>(R.id.textView_Game_Round)

        //get pending round
        viewModel.getPendingRound(currentUserId,levelId)
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
        if (!thinkManager.getTask(levelId))
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
            //showResult()
        }

        dialogFinishRound.onClickCancel ={
            gameSounds.playSoundErase()
            dialogFinishRound.dismiss()
        }

        gameSounds.playSoundRestart()
        dialogFinishRound.show()
    }


    private fun newTask() {
        //val str =
        thinkManager.newTask(LevelEntity(
            level.id,
            level.name,
            level.codeName,
            level.op1Min,
            level.op1Max,
            level.op2Min,
            level.op2Max,
            level.resMin,
            level.resMax,
            level.operation,
            level.equation,
            level.relationOp1,
            level.relationOp2))

        round.numTasks++
    }

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

        when (level.equation) {
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

        binding.buttonBack.setOnClickListener { finish() }
        binding.correctCounter.setOnClickListener { finishAndSave() }

        binding.button01.setOnClickListener { pressNum(1) }
        binding.button02.setOnClickListener { pressNum(2) }
        binding.button03.setOnClickListener { pressNum(3) }
        binding.button04.setOnClickListener { pressNum(4) }
        binding.button05.setOnClickListener { pressNum(5) }
        binding.button06.setOnClickListener { pressNum(6) }
        binding.button07.setOnClickListener { pressNum(7) }
        binding.button08.setOnClickListener { pressNum(8) }
        binding.button09.setOnClickListener { pressNum(9) }
        binding.button00.setOnClickListener { pressNum(0) }
    }

    companion object {
        const val EXTRA_LEVEL = "EXTRA_OPERATION_PARAMS"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"

        fun newInstance(context: Context, level: LevelEntity, userId:Int): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_LEVEL, level)
                putExtra(EXTRA_USER_ID,userId)
            }
        }
    }

}