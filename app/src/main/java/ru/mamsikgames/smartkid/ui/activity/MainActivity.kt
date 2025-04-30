package ru.mamsikgames.smartkid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.LevelsAdapter
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.databinding.ActivityMainBinding
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.ui.util.CenterLayoutManager
import ru.mamsikgames.smartkid.ui.adapters.LevelGroupsAdapter
import ru.mamsikgames.smartkid.ui.util.LevelGroupAdaptersAssociator
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel


class MainActivity : AppCompatActivity() {

    private var gameSounds = GameSounds
    private val viewModel: ChooseLevelViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private val adapterLevels = LevelsAdapter { openLevel(it) }
    private val adapterLevelGroups = LevelGroupsAdapter { clickGroup(it) }

    private var levelGroupsAdapterAssociator = listOf<LevelGroupAdaptersAssociator>()

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() ///

        gameSounds.initSounds(this)

        viewModel.currentUserId.observe(this) { if (it != null) userId = it }

        //LEVELS

        val levelLayoutManager = GridLayoutManager(this, 2)

        levelLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position in getSectionHeaders()) 2 else 1
            }
        }
        binding.recyclerLevels.layoutManager = levelLayoutManager
        binding.recyclerLevels.adapter = adapterLevels

        //LEVEL GROUPS

        binding.recyclerLevelGroups.layoutManager =
            CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerLevelGroups.adapter = adapterLevelGroups

        //Observe

        viewModel.listLevels.observe(this) { adapterLevels.setList(it) }
        viewModel.listLevelGroups.observe(this) { adapterLevelGroups.setList(it) }
        viewModel.listLevelGroupAdaptersAssociator.observe(this) {
            levelGroupsAdapterAssociator = it
        }
    }

    private fun openLevel(level: LevelModel) {
        gameSounds.playSoundPlay()
        val intent = GameActivity.newInstance(this, level.id, level.codeName, userId)
        startActivity(intent)
    }

    private fun clickGroup(groupPos: Int) {
        binding.recyclerLevelGroups.smoothScrollToPosition(groupPos)
        binding.recyclerLevels.smoothScrollToPosition(getLevelPos(groupPos))
    }

    private fun getLevelPos(groupPos: Int): Int {
        for (lga in levelGroupsAdapterAssociator) {
            if (lga.groupAdapterPos == groupPos) return lga.levelAdapterPos
        }
        return 0
    }

    private fun getSectionHeaders(): List<Int> {
        val list = mutableListOf<Int>()
        for (lga in levelGroupsAdapterAssociator) {
            list.add(lga.levelAdapterPos)
        }
        return list
    }
}


