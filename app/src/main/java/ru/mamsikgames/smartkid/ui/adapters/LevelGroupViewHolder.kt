package ru.mamsikgames.smartkid.ui.adapters

import android.graphics.Typeface
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.databinding.A4ItemLevelGroupBinding


class LevelGroupViewHolder(private val binding: A4ItemLevelGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(levelGroup: LevelGroupEntity) {
        binding.levelGroupName.text = levelGroup.name
    }

    fun setPaddingRight() {
        val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
        layoutParams.setMargins(0, 0, 430, 0)
    }

    fun setPaddingLeft() {
        val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
        //layoutParams.setMargins(330, 0, 30, 0)
        layoutParams.setMargins(430, 0, 0, 0)
    }

    fun selectItem():LevelGroupViewHolder {
        binding.levelGroupName.setTypeface(null, Typeface.BOLD)
        binding.root.pivotY = 75F
        binding.root.animate()
            .scaleX(1.5F)
            .scaleY(1.5F)
            .setDuration(500)
            .start()
        return this
    }

    fun unselectItem() {
        binding.root.pivotY = 75F
        binding.root.animate().scaleX(1F).scaleY(1F).setDuration(500).start()
        binding.levelGroupName.setTypeface(null, Typeface.NORMAL)
    }
}