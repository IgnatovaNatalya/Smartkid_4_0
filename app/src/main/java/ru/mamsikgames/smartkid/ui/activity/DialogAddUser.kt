package ru.mamsikgames.smartkid.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import ru.mamsikgames.smartkid.R


class DialogAddUser(
    _activity: Activity,
    themeResId:Int) : Dialog(_activity,themeResId) {

    lateinit var onClickButton: ((String) ->Unit)
    lateinit var onClickCancel: (() ->Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_user)

        val tvName = findViewById<EditText>(R.id.textView_AddUser_userName)


        val ivBtnOk = findViewById<ImageView>(R.id.imageView_Ok)
        ivBtnOk.setOnClickListener{
            val str = tvName.text.toString()
            onClickButton.invoke(str)
        }

        val ivBtnCancel = findViewById<ImageView>(R.id.imageView_Cancel)
        ivBtnCancel.setOnClickListener{
            onClickCancel.invoke()
        }

        this.setCanceledOnTouchOutside(true)

    }
}