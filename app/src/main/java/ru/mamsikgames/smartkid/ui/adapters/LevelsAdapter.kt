package ru.mamsikgames.smartkid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.data.entity.LevelEntity
import ru.mamsikgames.smartkid.databinding.A4ItemLevelBinding
import ru.mamsikgames.smartkid.ui.adapters.LevelViewHolder

class LevelsAdapter(private val clickListener: AdapterClickListener) :
    RecyclerView.Adapter<LevelViewHolder>() {

    private var levelsList = listOf<LevelEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return LevelViewHolder(A4ItemLevelBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        var sameGroup = if (position > 0)
            levelsList[position - 1].levelGroup == levelsList[position].levelGroup
        else false

        holder.bind(levelsList[position], sameGroup)
        holder.itemView.setOnClickListener { clickListener.onLevelClick(levelsList.get(position)) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<LevelEntity>) {
        levelsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = levelsList.size

    fun interface AdapterClickListener {
        fun onLevelClick(level: LevelEntity)
    }
}