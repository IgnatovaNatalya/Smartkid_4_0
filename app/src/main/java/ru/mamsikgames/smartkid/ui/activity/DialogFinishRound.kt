package ru.mamsikgames.smartkid.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import ru.mamsikgames.smartkid.R

class DialogFinishRound(
    activity: Activity,

    themeResId:Int) : Dialog(activity,themeResId) {

    lateinit var onClickButton: (() ->Unit)
    lateinit var onClickCancel: (() ->Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_finish_round)


        val ivBtnOk = findViewById<ImageView>(R.id.imageView_Ok)
        val ivBtnCancel = findViewById<ImageView>(R.id.imageView_Cancel)

        ivBtnOk.setOnClickListener{
            onClickButton.invoke()
            //onClickButton.invoke(round)
            //onClickButton.invoke()
        }

        ivBtnCancel.setOnClickListener{
            onClickCancel.invoke()
        }

        this.setCanceledOnTouchOutside(true)

    }




}