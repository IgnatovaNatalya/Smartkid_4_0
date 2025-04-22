package ru.mamsikgames.smartkid.core

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.media.SoundPool.*
import android.os.Handler
import android.os.Looper
import ru.mamsikgames.smartkid.R

object GameSounds {

    private var streamID = 0

    private var soundPlay= R.raw.sound_button_play
    private var soundButtons = R.raw.sound_button_menu

    private var soundKey = R.raw.sound_presskey
    private var soundErase = R.raw.sound_button_erase

    private var soundLevel = R.raw.sound_nextlevel
    private var soundCorrect= R.raw.sound_rise06
    private var soundRestart = R.raw.sound_button_restart
    private var soundExit = R.raw.sound_button_exit

    private var sPlay=0
    private var sButton = 0
    private var sKey = 0
    private var sErase =0
    private var sLevel =0
    private var sCor=0
    private var sRestart = 0
    private var sExit=0

    private var soundsCorrect = arrayOf(
        R.raw.sound_cor_horosho,
        R.raw.sound_cor_klass,
        R.raw.sound_cor_kruto,
        R.raw.sound_cor_molodetz,
        R.raw.sound_cor_molodetz_pravilno,
        R.raw.sound_cor_otlichno,
        R.raw.sound_cor_prevoshodno,
        R.raw.sound_cor_suuper,
        R.raw.sound_cor_tak_derzhat,
        R.raw.sound_cor_zamichatelno
    )

    private var soundsWrong = arrayOf(
        R.raw.sound_wrong_nepravilno,
        R.raw.sound_wrong_net1,
        R.raw.sound_wrong_neverno,
        R.raw.sound_wrong_poprobuj_eshe,
        R.raw.sound_wrong_poprobuj_eshe_raz,
        R.raw.sound_wrong_poprobuj_eshe_raz_1,
        R.raw.sound_wrong_zvuk_gadost
    )

    private var sCorrect = IntArray(soundsCorrect.size) { i -> i * 0 }
    private var sWrong = IntArray(soundsWrong.size) { i -> i * 0 }


    private lateinit var sPool:SoundPool

    fun initSounds(con:Context) {

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        sPool = Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(6)
            .build()
        sPlay = sPool.load(con, soundPlay, 1)
        sButton = sPool.load(con, soundButtons, 1)
        sKey = sPool.load(con, soundKey, 1)
        sErase = sPool.load(con, soundErase, 1)
        sLevel = sPool.load(con, soundLevel, 2)
        sCor=sPool.load(con, soundCorrect, 2)
        sRestart= sPool.load(con,soundRestart,1)
        sExit=sPool.load(con,soundExit,1)


        for (i in sCorrect.indices) {
            sCorrect[i] = sPool.load(con,soundsCorrect[i],1)
        }
        for (i in sWrong.indices) {
            sWrong[i] = sPool.load(con,soundsWrong[i],1)
        }

    }

    fun playSoundPlay() {
        playSound(sPlay)
    }
    fun playSoundCorrect(){
        playSound(sCorrect[(sCorrect.indices).random()])
    }
    fun playSoundWrong(){
        playSound(sWrong[(sWrong.indices).random()])
    }
    fun playSoundButton() {
        playSound(sKey)
    }
    fun playSoundErase() {
        playSound(sErase)
    }
    fun playSoundLevel() {
        playSound(sLevel)
    }

    fun playSoundCor(ms: Long) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            playSound(sCor)
        }, ms)
    }

    fun playSoundCor() {
        playSoundCor(0)
    }

    fun playSoundRestart() {
        playSound(sRestart)
    }

    fun playSoundExit() {
        playSound(sExit)
    }

    private fun playSound(sound: Int): Int {
        if (sound > 0) {
            streamID = sPool.play(sound, 1F, 1F, 1, 0, 1F)
        }
        return streamID
    }

}

