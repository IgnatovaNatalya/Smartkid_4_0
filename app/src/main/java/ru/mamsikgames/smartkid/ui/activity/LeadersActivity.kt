package ru.mamsikgames.smartkid.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.mamsikgames.smartkid.R
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.ui.viewmodel.SmartViewModel
import ru.mamsikgames.smartkid.ui.adapters.AdapterLeaders

class LeadersActivity : AppCompatActivity() {

    private lateinit var viewModel:SmartViewModel
    private var gameSounds = GameSounds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaders)
        supportActionBar?.hide() ///

        viewModel = ViewModelProvider(this)[SmartViewModel::class.java]

        val adapterLeaders = AdapterLeaders()
        val recyclerLeaders: RecyclerView = findViewById(R.id.recycler_Leaders)

        viewModel.getLeaders()
        viewModel.recordLeaders.observe(this) {
            if (it != null) {
                adapterLeaders.setList(it)
            }
        }
        recyclerLeaders.adapter = adapterLeaders

        val ivBtnBack = findViewById<ImageView>(R.id.imageView_Stat_buttonBack)
        ivBtnBack.setOnClickListener{
            gameSounds.playSoundExit()
            finish()//?
        }
    }
}