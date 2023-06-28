package com.omsi.composetemplate

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.omsi.composetemplate.data.repository.AudioRepository
import com.omsi.composetemplate.data.repository.CanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val canRepository: CanRepository,
    val audioRepository: AudioRepository
): ViewModel() {

    private val TAG = "MainViewModel"
    var canInitialized by mutableStateOf(false)
    var isAudioPlaying = false

    fun onStart(activity: Activity){
        // start task - the composable has entered the composition
        canInitialized = canRepository.initRepo(activity) { /* Do something*/ }
        Log.d(TAG, "Init can repository")
    }

    fun onStop(){
        // cancel task - the composable has left the composition
        canRepository.invalidate()
        canInitialized=false
        Log.d(TAG, "Invalidate can repository")
    }

    private fun byte2Int(byte:Byte):Int{
        return (byte.toUInt() and 255u).toInt()
    }


    fun setBuzzer(active:Boolean){
        if(active and !isAudioPlaying){
            audioRepository.play()
            isAudioPlaying=true
        }

        if(!active and isAudioPlaying){
            audioRepository.pause()
            isAudioPlaying=false
        }
    }



    override fun onCleared() {
        super.onCleared()
        audioRepository.pause()
        audioRepository.stop()
    }

}