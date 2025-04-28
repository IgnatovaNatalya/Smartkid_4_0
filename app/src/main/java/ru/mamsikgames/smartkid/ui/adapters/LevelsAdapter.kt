package ru.mamsikgames.smartkid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.databinding.A4ItemLevelBinding
import ru.mamsikgames.smartkid.domain.model.LevelModel
import ru.mamsikgames.smartkid.ui.adapters.LevelViewHolder

class LevelsAdapter(private val clickListener: AdapterClickListener) :
    RecyclerView.Adapter<LevelViewHolder>() {

    private var levelsList: List<LevelModel> = listOf<LevelModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return LevelViewHolder(A4ItemLevelBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        var sectionHeader = if (position > 0)
            levelsList[position - 1].groupName != levelsList[position].groupName
        else true

        holder.bind(levelsList.get(position) , sectionHeader)

        holder.itemView.setOnClickListener { clickListener.onLevelClick(levelsList[position]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<LevelModel>) {
        levelsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = levelsList.size

    fun interface AdapterClickListener {
        fun onLevelClick(level: LevelModel)
    }
}