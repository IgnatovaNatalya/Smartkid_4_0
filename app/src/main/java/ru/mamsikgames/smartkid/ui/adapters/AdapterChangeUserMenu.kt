package ru.mamsikgames.smartkid.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.data.entity.UserEntity

class AdapterChangeUserMenu(_selected: Int, _con: Context) :
    RecyclerView.Adapter<AdapterChangeUserMenu.UserMenuViewHolder>() {

    private var usersList = mutableListOf<UserEntity>()

    var selectedUser = _selected
    private var selectedPos = 0

    private val con: Context = _con

    lateinit var onClick: ((Int) -> Unit)

    class UserMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.textView_userName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserMenuViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserMenuViewHolder, position: Int) {
        val user = usersList[position]

        // if (user.userId == selectedUser) selectedPos = holder.bindingAdapterPosition //toDo

        val str = user.userName
        holder.tvUserName.text = str

        if (selectedPos == position) {
            holder.tvUserName.setTextColor(con.getColor(R.color.white))
        } else {
            holder.tvUserName.setTextColor(con.getColor(R.color.yellow))
        }

        holder.tvUserName.setOnClickListener {
            setSelected(position)
            selectedUser = usersList[position].userId
            onClick.invoke(usersList[position].userId)
        }
    }

    private fun setSelected(pos: Int) {
        notifyItemChanged(selectedPos)
        selectedPos = pos
        notifyItemChanged(selectedPos)
    }

    fun setList(list: List<UserEntity>) {
        usersList.clear()
        usersList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = usersList.size
}
