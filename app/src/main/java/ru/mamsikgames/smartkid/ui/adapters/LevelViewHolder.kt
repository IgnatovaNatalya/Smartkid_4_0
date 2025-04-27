package ru.mamsikgames.smartkid.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.databinding.A4ItemLevelBinding


class LevelViewHolder(private val binding: A4ItemLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(lvl: LevelEntity, sameGroup: Boolean) {

        binding.title.text = lvl.name
        binding.codename.text = lvl.codeName

        if (!sameGroup) {
            binding.sectionHeader.visibility = View.VISIBLE
            binding.sectionHeader.text = lvl.levelGroup.toString()
        }
        else binding.sectionHeader.visibility = View.GONE

    }
}