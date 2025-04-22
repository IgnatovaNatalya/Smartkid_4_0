package ru.mamsikgames.smartkid30

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.domain.model.RoundWithName
import ru.mamsikgames.smartkid.ui.TextViewOutline
import java.text.SimpleDateFormat
import java.util.*

class AdapterStat : RecyclerView.Adapter<AdapterStat.StatViewHolder>() {


    private var roundsList = mutableListOf<RoundWithName>()
    //private lateinit var onClick: ((RoundWithName) -> Unit)

    class StatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate:TextView = itemView.findViewById(R.id.textView_Stat_Date)
        private val tvLevelName:TextView =itemView.findViewById(R.id.textView_Stat_Level)
        private val tvNumCorrect:TextView = itemView.findViewById(R.id.textView_Stat_NumCorrect)
        private val tvNumWrong:TextView = itemView.findViewById(R.id.textView_Stat_NumWrong)
        //private val tvNumExits:TextViewOutline = itemView.findViewById(R.id.textView_Stat_NumExits)
        private val tvNumExits:TextViewOutline = itemView.findViewById(R.id.textView_Stat_NumExits)

        fun bind(roundWN:RoundWithName, sameDate:Boolean) {

            fun showDate(show:Boolean,strDate:String?) {
                val strToday = "Сегодня"
                val strYesterday = "Вчера"
                if(show) {
                    tvDate.isVisible = true
                    if (strDate!=null)tvDate.text=strDate
                    if (DateUtils.isToday(roundWN.roundEnd)) tvDate.text = strToday
                    if (DateUtils.isToday(roundWN.roundEnd + DateUtils.DAY_IN_MILLIS )) tvDate.text = strYesterday
                }
                else{
                    tvDate.isVisible = false
                }
            }
            val simpleDateFormat = SimpleDateFormat("dd.MM.yy", Locale.ROOT)
            showDate(!sameDate, simpleDateFormat.format(roundWN.roundEnd))

            tvLevelName.text = roundWN.codeName
            tvNumCorrect.text = roundWN.numCorrect.toString()

            //val str = "${roundWN.numCorrect}/${roundWN.numWrong}/${roundWN.numEfforts}/${roundWN.numExits}"
            tvNumWrong.text = roundWN.numWrong.toString()
            tvNumExits.text = roundWN.numExits.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stat, parent, false)
        return StatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        var sameDate = false
        val simpleDateFormat = SimpleDateFormat("dd.MM.yy", Locale.ROOT)
        if (position>0) sameDate = simpleDateFormat.format(roundsList[position-1].roundEnd) == simpleDateFormat.format(roundsList[position].roundEnd)
        //holder.tvLevelName.setOnClickListener { onClick.invoke(roundsList[position]) }//todo
        holder.bind(roundsList[position],sameDate)
    }

    fun setList(list: List<RoundWithName>) {
        roundsList.clear()
        roundsList.addAll(list)
        notifyDataSetChanged()
    }
    override fun getItemCount() = roundsList.size

}