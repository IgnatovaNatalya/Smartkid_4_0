package ru.mamsikgames.smartkid.ui.adapters

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.databinding.A4ItemLevelGroupBinding


class LevelGroupViewHolder(private val binding: A4ItemLevelGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(levelGroup: LevelGroupEntity) {
        binding.levelGroupName.text = levelGroup.name
        binding.root.scaleX = 0.5F
        binding.root.scaleY = 0.5F
    }

    fun setPaddingRight() {
        val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
        layoutParams.setMargins(0, 0, 500, 0)
    }

    fun setPaddingLeft() {
        val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
        layoutParams.setMargins(500, 0, 0, 0)
    }

    fun selectItem() {
        binding.root.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.increase)
        binding.root.animate()
    }
}