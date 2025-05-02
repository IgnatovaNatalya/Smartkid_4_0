package ru.mamsikgames.smartkid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.databinding.ActivityGameBinding
import ru.mamsikgames.smartkid.domain.model.Round
import ru.mamsikgames.smartkid.ui.viewmodel.GameViewModel
import ru.mamsikgames.smartkid.ui.viewmodel.TaskRenderParams


class GameActivity : AppCompatActivity() {

    private val gameSounds: GameSounds by inject()
    private val viewModel: GameViewModel by viewModel()

    private lateinit var binding: ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide() ///

        val userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        val levelId = intent.getIntExtra(EXTRA_LEVEL_ID, 0)
        val levelCodeName = intent.getStringExtra(EXTRA_LEVEL_CODENAME)

        binding.textViewGameLevelName.text = levelCodeName

        setListeners()

        viewModel.start(userId, levelId)
        viewModel.taskRenderParams.observe(this) { renderTask(it) }
        viewModel.roundRenderParams.observe(this) { renderRound(it) }

        viewModel.answerState.observe(this) { renderAnswer(it) }
    }

    private fun renderRound(r: Round) {
        setCorrect(r.numCorrect)
        setWrong(r.numWrong)
    }

    private fun renderTask(taskRenderParams: TaskRenderParams) {
        val str = taskRenderParams.taskStr

        if (taskRenderParams.spanStart >= 0) {
            val string = SpannableString(str)
            string.setSpan(
                UnderlineSpan(),
                taskRenderParams.spanStart,
                taskRenderParams.spanEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.task.text = string
        } else
            binding.task.text = str

        setOkButtonState(taskRenderParams.btnEnterState)
        setEraseButtonState(taskRenderParams.btnEraseState)
    }

    private fun renderAnswer(correct:Boolean) {
        if (correct) {
            gameSounds.playSoundCor()
            gameSounds.playSoundCorrect()
        }
        else {
            gameSounds.playSoundWrong()
        }
    }

    private fun pressNum(pressedNum: Int) {
        gameSounds.playSoundButton()
        viewModel.pressNum(pressedNum)
    }

    private fun setCorrect(cor: Int?) {
        binding.correctCounter.text = cor.toString()
    }

    private fun setWrong(wr: Int?) {
        binding.wrongCounter.text = wr.toString()
    }

    private fun setOkButtonState(state: Boolean) {
        with (binding.buttonEnter) {
            if (state) setImageResource(R.drawable.a4_btn_enter)
            else setImageResource(R.drawable.a4_btn_enter_inactive)
        }
    }

    private fun setEraseButtonState(state: Boolean) {
        if (state) binding.buttonErase.setImageResource(R.drawable.a4_btn_erase)
        else binding.buttonErase.setImageResource(R.drawable.a4_btn_erase_inactive)
    }

    private fun setListeners() {
        binding.buttonEnter.setOnClickListener {
            gameSounds.playSoundPlay()
            viewModel.pressOK()
        }

        binding.buttonErase.setOnClickListener {
            gameSounds.playSoundErase()
            viewModel.pressErase()
        }

        binding.buttonBack.setOnClickListener { finish() }

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

    private fun doExit() {
        viewModel.completeRound()
        gameSounds.playSoundExit()
    }

    override fun onPause() {
        doExit()
        super.onPause()
    }

    companion object {
        const val EXTRA_LEVEL_ID = "EXTRA_LEVEL_ID"
        const val EXTRA_LEVEL_CODENAME = "EXTRA_LEVEL_CODENAME"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"

        fun newInstance(
            context: Context,
            levelId: Int,
            levelCodeName: String,
            userId: Int
        ): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_LEVEL_ID, levelId)
                putExtra(EXTRA_LEVEL_CODENAME, levelCodeName)
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }
}