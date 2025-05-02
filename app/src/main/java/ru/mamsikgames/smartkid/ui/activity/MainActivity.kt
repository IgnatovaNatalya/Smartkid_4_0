package ru.mamsikgames.smartkid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.LevelsAdapter
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.databinding.ActivityMainBinding
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.ui.adapters.LevelGroupsAdapter
import ru.mamsikgames.smartkid.ui.util.CenterLayoutManager
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel

class MainActivity : AppCompatActivity() {

    private var gameSounds = GameSounds
    private val viewModel: ChooseLevelViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private val adapterLevels = LevelsAdapter { openLevel(it) }
    private val adapterLevelGroups = LevelGroupsAdapter { clickGroup(it) }

    private var levelGroupsMap = mapOf<Int, Int>()

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
                return if (position in levelGroupsMap.keys) 2 else 1
            }
        }

        binding.recyclerLevels.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when (val i = levelLayoutManager.findFirstVisibleItemPosition()) {
                    i -> if (i in levelGroupsMap.keys) switchGroup(i)
                }
            }
        })

        binding.recyclerLevels.layoutManager = levelLayoutManager
        binding.recyclerLevels.adapter = adapterLevels

        //LEVEL GROUPS

        binding.recyclerLevelGroups.layoutManager =
            CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerLevelGroups.adapter = adapterLevelGroups

        //Observe

        viewModel.listLevels.observe(this) { adapterLevels.setList(it) }
        viewModel.listLevelGroups.observe(this) { adapterLevelGroups.setList(it) }
        viewModel.mapGroupLevels.observe(this) { levelGroupsMap = it }
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

    private fun switchGroup(levelPos: Int) {
        binding.recyclerLevelGroups.smoothScrollToPosition(getGroupPos(levelPos))
    }

    private fun getLevelPos(groupPos: Int): Int {
        for (lgm in levelGroupsMap)
            if (lgm.value == groupPos) return lgm.key
        return 0
    }

    private fun getGroupPos(levelPos: Int): Int {
        return levelGroupsMap[levelPos]?:0
    }

}


