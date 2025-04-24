package ru.mamsikgames.smartkid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.data.entity.LevelEntity

class AdapterLevels : RecyclerView.Adapter<AdapterLevels.MainMenuViewHolder>() {

    private var levelsList = mutableListOf<LevelEntity>()
    lateinit var onClick: ((LevelEntity) -> Unit)

    inner class MainMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLevelName: TextView = itemView.findViewById(R.id.TextView_Level_name)
        private val tvLevel: TextView = itemView.findViewById(R.id.TextView_Level)
        private val ivButton: ImageView = itemView.findViewById(R.id.imageView_bgLevel)
        private var llLevelGroup: ConstraintLayout = itemView.findViewById(R.id.ll_levelGroup)
        private var tvLevelGroup: TextView = itemView.findViewById(R.id.TextView_levelGroup)

        fun bind(lvl: LevelEntity, sameGroup: Boolean) {

            tvLevelName.text = lvl.name
            tvLevel.text = lvl.codeName

            tvLevelName.setOnClickListener { onClick.invoke(lvl) }
            ivButton.setOnClickListener { onClick.invoke(lvl) }

//            fun showGroup(show: Boolean, levelGroup: Int?) {
//                if (show) {
//                    llLevelGroup.isVisible = true
//                    tvLevelGroup.text = levelGroup
//                } else
//                    llLevelGroup.isVisible = false
//            }
            //showGroup(!sameGroup, lvl.levelGroup)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main, parent, false)
        return MainMenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        var sameGroup = false
        if (position > 0) sameGroup =
            levelsList[position - 1].operation == levelsList[position].operation
        holder.bind(levelsList[position], sameGroup)
    }

    fun setList(list: List<LevelEntity>) {
        levelsList.clear()
        levelsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = levelsList.size

}