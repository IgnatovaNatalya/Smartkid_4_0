package ru.mamsikgames.smartkid.ui.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.core.BindingFragment
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.databinding.FragmentGameBinding
import ru.mamsikgames.smartkid.domain.model.Round
import ru.mamsikgames.smartkid.ui.viewmodel.GameViewModel
import ru.mamsikgames.smartkid.ui.viewmodel.TaskRenderParams
import kotlin.getValue

class GameFragment : BindingFragment<FragmentGameBinding>() {

    private val gameSounds: GameSounds by inject()
    private val viewModel: GameViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentGameBinding {
        return FragmentGameBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = requireArguments().getInt(EXTRA_USER_ID, 0)
        val levelId = requireArguments().getInt(EXTRA_LEVEL_ID, 0)
        val levelCodeName = requireArguments().getString(EXTRA_LEVEL_CODENAME)

        binding.textViewGameLevelName.text = levelCodeName

        setListeners()

        viewModel.start(userId, levelId)
        viewModel.taskRenderParams.observe(viewLifecycleOwner) { renderTask(it) }
        viewModel.roundRenderParams.observe(viewLifecycleOwner) { renderRound(it) }

        viewModel.answerState.observe(viewLifecycleOwner) { renderAnswer(it) }
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

    private fun renderAnswer(correct: Boolean) {
        if (correct) {
            gameSounds.playSoundCor()
            gameSounds.playSoundCorrect()
        } else {
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
        with(binding.buttonEnter) {
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

        //binding.buttonBack.setOnClickListener { finish() }

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

        fun createArgs(levelId: Int, codeName: String, userId: Int): Bundle =
            bundleOf(
                EXTRA_LEVEL_ID to levelId,
                EXTRA_LEVEL_CODENAME to codeName,
                EXTRA_USER_ID to userId
            )
    }
}