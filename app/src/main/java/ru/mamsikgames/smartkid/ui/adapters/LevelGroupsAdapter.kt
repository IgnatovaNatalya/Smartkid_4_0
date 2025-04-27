package ru.mamsikgames.smartkid.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.data.entity.LevelGroupEntity
import ru.mamsikgames.smartkid.databinding.A4ItemLevelGroupBinding

class LevelGroupsAdapter(private val clickGroupListener: AdapterClickListener) :
    RecyclerView.Adapter<LevelGroupViewHolder>() {

    private var levelGroupsList = listOf<LevelGroupEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelGroupViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return LevelGroupViewHolder(A4ItemLevelGroupBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: LevelGroupViewHolder, position: Int) {
        holder.bind(levelGroupsList[position])
        holder.itemView.setOnClickListener {
            clickGroupListener.onLevelGroupClick(position)
            holder.selectItem()
        }

        if (position == levelGroupsList.size - 1) holder.setPaddingRight()
        if (position == 0) holder.setPaddingLeft()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<LevelGroupEntity>) {
        levelGroupsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = levelGroupsList.size

    fun interface AdapterClickListener {
        fun onLevelGroupClick(position: Int)

    }
}