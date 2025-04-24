package ru.mamsikgames.smartkid.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.data.entity.UserEntity
import java.io.Serializable
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.AdapterLevels
import ru.mamsikgames.smartkid.databinding.ActivityMainBinding
import ru.mamsikgames.smartkid.domain.SmartRepository
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private var gameSounds = GameSounds
    private val viewModel: ChooseLevelViewModel  by viewModel()
    private lateinit var binding: ActivityMainBinding

    private var currentUser = UserEntity(0, "",  true)

    private val smartRepository: SmartRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() ///

        //viewModel = ViewModelProvider(this)[ChooseLevelViewModel::class.java]

        gameSounds.initSounds(this)

        //binding.textViewPlayer.setOnClickListener { showChangeUser() }

        //viewModel.getListUsers()
        viewModel.getCurrentUser()
        viewModel.recordCurUser.observe(this) {
            if (it != null) {
                currentUser = it
                val str = " ${currentUser.userName} "
                binding.textViewPlayer.text = str
            }
        }

//main recycler



        val adapterOperations = AdapterLevels()
        binding.recyclerOperations.adapter = adapterOperations

        viewModel.getListOperations()
        viewModel.recordOperations.observe(this) {
            if (it != null) {
                adapterOperations.setList(it)
            }
        }

        adapterOperations.onClick = { oper: LevelEntity ->
            gameSounds.playSoundPlay()

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(EXTRA_OPERATION, oper as Serializable)
            intent.putExtra(EXTRA_USER_ID, currentUser.userId as Serializable)
            intent.putExtra(EXTRA_USER_NAME, currentUser.userName as Serializable)

            startActivity(intent)
        }

        val snapHelperM: SnapHelper = PagerSnapHelper()
        snapHelperM.attachToRecyclerView(binding.recyclerOperations)

    }

    companion object {
        const val EXTRA_OPERATION = "EXTRA_OPERATION_PARAMS"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }



}


