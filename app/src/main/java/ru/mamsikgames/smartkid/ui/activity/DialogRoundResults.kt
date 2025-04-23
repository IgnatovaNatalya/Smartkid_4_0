package ru.mamsikgames.smartkid.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import ru.mamsikgames.smartkid.data.entity.RoundEntity
import ru.mamsikgames.smartkid.ui.TextViewOutline
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import ru.mamsikgames.smartkid.R


class DialogRoundResults( activity: Activity,themeResId:Int,
                          _round:RoundEntity,_level:String,_place:Int) : Dialog(activity,themeResId) {

    private val round = _round
    private val level = _level
    private val place = _place

    lateinit var onClickButton: (() ->Unit)
    lateinit var onClickCancel: (() ->Unit)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_round_results)

        val tvLevel = findViewById<TextViewOutline>(R.id.textView_Results_Level)
        //val tvTasks = findViewById<TextViewOutline>(R.id.textView_Results_NumCorrect)

        val tvCorrect = findViewById<TextViewOutline>(R.id.textView_Results_NumCorrect)
        val tvWrong = findViewById<TextViewOutline>(R.id.textView_Results_NumWrong)
        //val tvTries = findViewById<TextViewOutline>(R.id.textView_Results_Tries)

        val tvStart = findViewById<TextView>(R.id.textView_Results_TimeStart)
        val tvFinish = findViewById<TextView>(R.id.textView_Results_TimeFinish)
        val tvDuration = findViewById<TextViewOutline>(R.id.textView_Results_Duration)
        val tvExits = findViewById<TextView>(R.id.textView_Results_Exits)

        val ivBadge = findViewById<ImageView>(R.id.imageView_Results_Badge)

        val ivBtnOk = findViewById<ImageView>(R.id.imageView_Ok)
        val ivBtnCancel = findViewById<ImageView>(R.id.imageView_Cancel)

//level tasks etc
        tvLevel.text = level
        //tvTasks.text = round.numTasks.toString()
        tvCorrect.text = round.numCorrect.toString()
        tvWrong.text = round.numWrong.toString()
        //tvTries.text = round.numEfforts.toString()

//badge
        when (place) {
            1 -> ivBadge.setImageResource(R.drawable.badge_1)
            2 -> ivBadge.setImageResource(R.drawable.badge_2)
            3 -> ivBadge.setImageResource(R.drawable.badge_3)
            4 -> ivBadge.setImageResource(R.drawable.badge_4)
            5 -> ivBadge.setImageResource(R.drawable.badge_5)
            else  -> ivBadge.setImageResource(R.drawable.badge_5)
        }

//datetime
        val simpleDateFormat = SimpleDateFormat("dd.MM HH:mm")
        val beginStr =simpleDateFormat.format(round.roundBegin)
        val endStr = simpleDateFormat.format(round.roundEnd)
        tvStart.text = beginStr
        tvFinish.text = endStr

//duration
        val h = TimeUnit.MILLISECONDS.toHours(round.duration)
        val m = TimeUnit.MILLISECONDS.toMinutes(round.duration - h*60)
        val s = TimeUnit.MILLISECONDS.toSeconds(round.duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(round.duration))

        var durStr=""
        if ( h>0 )  durStr = "$h ч"
        if ( (m>0) || (durStr!="") ) durStr += " $m мин"
        if ( (s>0) || (durStr!="") ) durStr += " $s сек"

        tvDuration.text = durStr

//exits
        tvExits.text = round.numExits.toString()

        ivBtnOk.setOnClickListener{
            onClickButton.invoke()
        }

        ivBtnCancel.setOnClickListener{
            onClickCancel.invoke()
        }

        this.setCanceledOnTouchOutside(true)

    }

}