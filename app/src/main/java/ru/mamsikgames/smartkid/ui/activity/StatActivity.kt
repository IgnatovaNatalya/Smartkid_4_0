package ru.mamsikgames.smartkid.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.ui.util.TextViewOutline
import ru.mamsikgames.smartkid.ui.viewmodel.SmartViewModel
import ru.mamsikgames.smartkid.ui.adapters.AdapterStat

class StatActivity : AppCompatActivity() {

    private var currentUserId = 1
    private var currentUserName = ""
    private lateinit var viewModel:SmartViewModel
    private var gameSounds = GameSounds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)
        supportActionBar?.hide() ///

        currentUserId = intent.getIntExtra(EXTRA_USER_ID,0)
        currentUserName = intent.getStringExtra(EXTRA_USER_NAME).toString()


        val tvUserName = findViewById<TextViewOutline>(R.id.textView_Stat_userName)
        tvUserName.text = currentUserName

        viewModel = ViewModelProvider(this)[SmartViewModel::class.java]


        val adapterStat = AdapterStat()
        val recyclerStat: RecyclerView = findViewById(R.id.recycler_Stat)

        viewModel.getListRoundsWithNames(currentUserId)
        viewModel.recordRounds.observe(this) {
            if (it != null) {
                adapterStat.setList(it)
            }
        }
        recyclerStat.adapter = adapterStat

        val ivBtnBack = findViewById<ImageView>(R.id.imageView_Stat_buttonBack)
        ivBtnBack.setOnClickListener{
            gameSounds.playSoundExit()
            finish()//?
        }
    }

    companion object {//повторяются
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }
}