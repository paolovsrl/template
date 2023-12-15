package com.omsi.composetemplate.data.repository

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.omsi.composetemplate.R
import com.omsi.tonegenerator.CustomToneGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(@ApplicationContext val context:Context){

    private var mediaPlayer: MediaPlayer
    private var audioManager:AudioManager
    public var isPlaying = false
    var toneGenerator = CustomToneGenerator

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
        if(!isPlaying && !mediaPlayer.isPlaying) {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 3,
                0
            )
            //mediaPlayer.start()
            toneGenerator.startAudioStreamNative()
            isPlaying = true
        }
    }


    fun pause(){
        if(isPlaying) {
            //mediaPlayer.pause()
            toneGenerator.stopAudioStreamNative()
            isPlaying = false
        }
    }


    fun stop(){
        try {
            // mediaPlayer.stop()
            // mediaPlayer.release()
            toneGenerator.stopAudioStreamNative()
        }catch (error:Error){
            Log.e("AudioRepository", "Can't stop MediaPlayer: "+error.message)
        }

    }



}