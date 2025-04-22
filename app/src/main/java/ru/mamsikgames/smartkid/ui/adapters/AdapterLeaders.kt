package ru.mamsikgames.smartkid30

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class AdapterLeaders : RecyclerView.Adapter<AdapterLeaders.LeadersViewHolder>() {


    private var leadersList = mutableListOf<Leader>()

    class LeadersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName:TextView = itemView.findViewById(R.id.textView_itemLeaders_Name)
        private val tvNumTasks:TextView = itemView.findViewById(R.id.textView_itemLeaders_NumTasks)
        private val tvRate:TextView = itemView.findViewById(R.id.textView_itemLeaders_Rate)
        private val ivBadge: ImageView = itemView.findViewById(R.id.imageView_itemLeaders_Badge)

        private val ivStar1 : ImageView = itemView.findViewById(R.id.imageView_itemLeaders_star1)
        private val ivStar2 : ImageView = itemView.findViewById(R.id.imageView_itemLeaders_star2)
        private val ivStar3 : ImageView = itemView.findViewById(R.id.imageView_itemLeaders_star3)
        private val ivStar4 : ImageView = itemView.findViewById(R.id.imageView_itemLeaders_star4)
        private val ivStar5 : ImageView = itemView.findViewById(R.id.imageView_itemLeaders_star5)

        private val starsArray = arrayOf(ivStar1,ivStar2,ivStar3,ivStar4,ivStar5)

        fun bind(leader:Leader,pos:Int) {
            tvName.text = leader.userName
            tvNumTasks.text = leader.tasks.toString()

            val points = leader.rate

            for ( (i,s) in starsArray.withIndex() ) {
                var n =points.toFloat()/((i+1)*20)
                if (n >= 1)
                    s.setImageResource(R.drawable.star)
                else s.setImageResource(R.drawable.star_shadow)
            }

            val str =leader.rate.toString() + "%"
            tvRate.text = str

            when (pos) {
                0 -> ivBadge.setImageResource(R.drawable.badge_1)
                1 -> ivBadge.setImageResource(R.drawable.badge_2)
                2 -> ivBadge.setImageResource(R.drawable.badge_3)
                3 -> ivBadge.setImageResource(R.drawable.badge_4)
                4 -> ivBadge.setImageResource(R.drawable.badge_5)
                else -> ivBadge.setImageResource(R.drawable.badge_5)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadersViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leader, parent, false)
        return LeadersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LeadersViewHolder, position: Int) {
        holder.bind(leadersList[position],position)
    }

    fun setList(list: List<Leader>) {
        leadersList.clear()
        leadersList.addAll(list)
        notifyDataSetChanged()
    }
    override fun getItemCount() = leadersList.size

}