package ru.mamsikgames.smartkid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.core.BindingFragment
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.databinding.FragmentLevelsBinding
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.ui.adapters.LevelGroupsAdapter
import ru.mamsikgames.smartkid.ui.adapters.LevelsAdapter
import ru.mamsikgames.smartkid.ui.util.CenterLayoutManager
import ru.mamsikgames.smartkid.ui.viewmodel.ChooseLevelViewModel

class LevelsFragment : BindingFragment<FragmentLevelsBinding>() {

    private var gameSounds = GameSounds
    private val viewModel: ChooseLevelViewModel by viewModel()

    private val adapterLevels = LevelsAdapter { openLevel(it) }
    private val adapterLevelGroups = LevelGroupsAdapter { clickGroup(it) }

    private var levelGroupsMap = mapOf<Int, Int>()

    private var userId = 0

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentLevelsBinding {
        return FragmentLevelsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameSounds.initSounds(requireContext())

        viewModel.currentUserId.observe(viewLifecycleOwner) { if (it != null) userId = it } ///

        //LEVELS

        val levelLayoutManager = GridLayoutManager(requireContext(), 2)

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
            CenterLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerLevelGroups.adapter = adapterLevelGroups

        //Observe

        viewModel.listLevels.observe(viewLifecycleOwner) { adapterLevels.setList(it) }
        viewModel.listLevelGroups.observe(viewLifecycleOwner) { adapterLevelGroups.setList(it) }
        viewModel.mapGroupLevels.observe(viewLifecycleOwner) { levelGroupsMap = it }
    }

    private fun openLevel(level: LevelModel) {
        gameSounds.playSoundPlay()
        findNavController().navigate(
            R.id.action_levelsFragment_to_gameFragment,
            GameFragment.createArgs(level.id, level.codeName, userId)
        )
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
        return levelGroupsMap[levelPos] ?: 0
    }
}