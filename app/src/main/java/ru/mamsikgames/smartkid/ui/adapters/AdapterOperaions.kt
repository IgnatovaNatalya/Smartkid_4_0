package ru.mamsikgames.smartkid30

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class AdapterOperations : RecyclerView.Adapter<AdapterOperations.MainMenuViewHolder>() {

    private var operationsList = mutableListOf<Operation>()
    lateinit var onClick: ((Operation) -> Unit)

    inner class MainMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLevelName:TextView =itemView.findViewById(R.id.TextView_Level_name)
        private val tvLevel:TextView =itemView.findViewById(R.id.TextView_Level)
        private val ivButton:ImageView = itemView.findViewById(R.id.imageView_bgLevel)
        private var llLevelGroup:ConstraintLayout = itemView.findViewById(R.id.ll_levelGroup)
        private var tvLevelGroup:TextView = itemView.findViewById(R.id.TextView_levelGroup)

        fun bind(oper: Operation, sameGroup:Boolean) {

            tvLevelName.text = oper.name
            tvLevel.text = oper.codeName

            tvLevelName.setOnClickListener { onClick.invoke(oper) }
            ivButton.setOnClickListener { onClick.invoke(oper) }

            fun showGroup(show: Boolean, str: String?) {
                if (show) {
                    llLevelGroup.isVisible = true
                    tvLevelGroup.text = str
                } else
                    llLevelGroup.isVisible = false
            }
            showGroup(!sameGroup, oper.groupName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main, parent, false)
        return MainMenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        var sameGroup =false
        if (position>0) sameGroup=  operationsList[position-1].operation == operationsList[position].operation
        holder.bind(operationsList[position],sameGroup)
    }

    fun setList(list: List<Operation>) {
        operationsList.clear()
        operationsList.addAll(list)
        notifyDataSetChanged()
    }
    override fun getItemCount() = operationsList.size

}