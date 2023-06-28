package com.omsi.composetemplate.data.repository

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import com.omsi.composetemplate.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(@ApplicationContext context:Context){

    private var mediaPlayer: MediaPlayer
    private var audioManager:AudioManager

    init {
        val audioAttributes =  AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        mediaPlayer = MediaPlayer.create(context, R.raw.khz)
            .apply {
                this.setAudioAttributes(audioAttributes)
                this.isLooping = true
            }

        audioManager= context.getSystemService(AUDIO_SERVICE) as AudioManager
    }


    fun play(){
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0)
        mediaPlayer.start()
    }


    fun pause(){
        mediaPlayer.pause()
    }


    fun stop(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }



}