package ru.mamsikgames.smartkid.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.databinding.A4ItemLevelBinding
import ru.mamsikgames.smartkid.domain.model.LevelModel


class LevelViewHolder(private val binding: A4ItemLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(lvl: LevelModel, sectionHeader: Boolean) {

        binding.title.text = lvl.name
        binding.codename.text = lvl.codeName

        if (sectionHeader) {
            binding.sectionHeader.visibility = View.VISIBLE
            binding.sectionHeader.text = lvl.groupName.toString()
        }
        else binding.sectionHeader.visibility = View.GONE

    }
}