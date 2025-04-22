package ru.mamsikgames.smartkid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import ru.mamsikgames.smartkid.core.GameSounds
import ru.mamsikgames.smartkid.data.db.entity.User
import java.io.Serializable
import ru.mamsikgames.smartkid.R

class MainActivity : AppCompatActivity() {

    private var gameSounds = GameSounds
    //private lateinit var currentUser: User
    private var currentUser=User(0,"",0,"",true)
    private lateinit var viewModel: SmartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() ///

        //val musicIntent = Intent(this, BackgroundMusic::class.java)
        //startService(musicIntent)

        gameSounds.initSounds(this)

        viewModel = ViewModelProvider(this)[SmartViewModel::class.java]

        val tvUserName: TextViewOutline = findViewById(R.id.textView_Player)
        tvUserName.setOnClickListener{showChangeUser()}

        viewModel.getListUsers()
        viewModel.getCurrentUser()
        viewModel.recordCurUser.observe(this) {
            if (it != null) {
                currentUser = it
                val str = " ${currentUser.userName} "
                tvUserName.text = str
            }
        }

        viewModel.recordCountUsers.observe(this) {
            if (it==0) showAddUser()
        }

        val ivMenuStat = findViewById<ImageView>(R.id.imageView_Main_MenuStat)
        ivMenuStat.setOnClickListener {
            gameSounds.playSoundButton()
            val intent = Intent(this, StatActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, currentUser.userId as Serializable)
            intent.putExtra(EXTRA_USER_NAME, currentUser.userName as Serializable)
            startActivity(intent)
        }

        val ivMenuLeaders = findViewById<ImageView>(R.id.imageView_Main_MenuLeaders)
        ivMenuLeaders.setOnClickListener {
            gameSounds.playSoundButton()
            val intent = Intent(this, LeadersActivity::class.java)
            startActivity(intent)
        }

//main recycler
        val adapterOperations = AdapterOperations()
        val recyclerOperations: RecyclerView = findViewById(R.id.recycler_Operations)

        viewModel.getListOperations()
        viewModel.recordOperations.observe(this) {
            if (it != null) {
                adapterOperations.setList(it)
            }
        }

        adapterOperations.onClick = { oper:Operation ->
            gameSounds.playSoundPlay()

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(EXTRA_OPERATION, oper as Serializable)
            intent.putExtra(EXTRA_USER_ID, currentUser.userId as Serializable)
            intent.putExtra(EXTRA_USER_NAME, currentUser.userName as Serializable)

            startActivity(intent)
        }

        recyclerOperations.adapter = adapterOperations

        val snapHelperM: SnapHelper = PagerSnapHelper()
        snapHelperM.attachToRecyclerView(recyclerOperations)
    }


    private fun showChangeUser() {
        val dialogChangeUser =
            DialogChangeUser(this, currentUser.userId, viewModel, R.style.dialog_style)

        dialogChangeUser.onClickButton = { userId ->
            gameSounds.playSoundPlay()
            viewModel.setCurrentUser(userId)
            dialogChangeUser.dismiss()
        }
        dialogChangeUser.onClickCancel = {
            gameSounds.playSoundErase()
            dialogChangeUser.dismiss()
        }
        dialogChangeUser.onClickRecyclerItem ={
            gameSounds.playSoundButton()
        }
        gameSounds.playSoundRestart()
        dialogChangeUser.show()

    }

    private fun showAddUser() {
        val dialogAddUser =
            DialogAddUser(this, R.style.dialog_style)

        dialogAddUser.onClickButton = {strName->
            viewModel.addUser(strName)
            gameSounds.playSoundPlay()
            dialogAddUser.dismiss()
        }
        dialogAddUser.onClickCancel = {
            gameSounds.playSoundErase()
            dialogAddUser.dismiss()
        }
        gameSounds.playSoundRestart()
        dialogAddUser.show()
    }

    override fun onStop() {
        stopService(Intent(this, BackgroundMusic::class.java))
        super.onStop()
    }
}
const val EXTRA_OPERATION = "EXTRA_OPERATION_PARAMS"
const val EXTRA_USER_ID = "EXTRA_USER_ID"
const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
