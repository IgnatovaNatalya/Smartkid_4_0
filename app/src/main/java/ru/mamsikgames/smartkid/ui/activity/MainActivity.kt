package ru.mamsikgames.smartkid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.LevelsAdapter
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.databinding.ActivityMainBinding
import ru.mamsikgames.smartkid.ui.CenterLayoutManager
import ru.mamsikgames.smartkid.ui.adapters.LevelGroupsAdapter
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel


class MainActivity : AppCompatActivity() {

    private var gameSounds = GameSounds
    private val viewModel: ChooseLevelViewModel  by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val adapterLevels = LevelsAdapter { openLevel(it) }
    private val adapterLevelGroups = LevelGroupsAdapter { selectGroup(it) }

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() ///

        gameSounds.initSounds(this)

        viewModel.getCurrentUser()
        viewModel.currentUser.observe(this) {
            if (it != null) userId = it.userId //todo передавать только Id
//                currentUser = it
//                val str = " ${currentUser.userName} "
//                binding.textViewPlayer.text = str
        }

        binding.recyclerLevels.adapter = adapterLevels

        viewModel.listLevels.observe(this) {
            adapterLevels.setList(it)
        }

        binding.recyclerLevelGroups.layoutManager = CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerLevelGroups.adapter = adapterLevelGroups


        viewModel.listLevelGroups.observe(this) {
            adapterLevelGroups.setList(it)
        }

//        val snapHelperM: SnapHelper = PagerSnapHelper()
//        snapHelperM.attachToRecyclerView(binding.recyclerLevelGroups)
    }

    private fun openLevel(lvl: LevelEntity) {
        gameSounds.playSoundPlay()
        val intent = GameActivity.newInstance(this, lvl, userId)
        startActivity(intent)
    }

    private fun selectGroup(pos: Int) {
        binding.recyclerLevelGroups.smoothScrollToPosition(pos)
    }

}


