package ru.mamsikgames.smartkid.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.ui.viewmodel.SmartViewModel
import ru.mamsikgames.smartkid30.AdapterChangeUserMenu
import ru.mamsikgames.smartkid.R


class DialogChangeUser(
    _activity: Activity,
    _currentUserId:Int,
    _viewModel: SmartViewModel,
    themeResId:Int) : Dialog(_activity,themeResId) {

    private val activity = _activity
    private val viewModel: SmartViewModel = _viewModel
    private val currentUserId=_currentUserId
    private var selectedUserId = currentUserId
    lateinit var onClickButton: ((Int) ->Unit)
    lateinit var onClickCancel: (() ->Unit)
    lateinit var onClickRecyclerItem : (() -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_change_user)


        val changeUserRecycler: RecyclerView = findViewById(R.id.recyclerView_ChangeUser)
        val adapter = AdapterChangeUserMenu(currentUserId,activity.applicationContext)

        viewModel.getListUsers()
        viewModel.recordUsers.observe(activity as LifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.setList(it)
            }
        }
        changeUserRecycler.adapter = adapter

        //changeUserRecycler.scrollToPosition(currentUserId)

        adapter.onClick = {it ->
            //adapter.setSelected(it)
            selectedUserId = it
            onClickRecyclerItem.invoke()
        }

        val ivBtnOk = findViewById<ImageView>(R.id.imageView_Ok)
        val ivBtnCancel = findViewById<ImageView>(R.id.imageView_Cancel)

        ivBtnOk.setOnClickListener{
            onClickButton.invoke(adapter.selectedUser)
        }

        ivBtnCancel.setOnClickListener{
            onClickCancel.invoke()
        }

        this.setCanceledOnTouchOutside(true)

    }




}