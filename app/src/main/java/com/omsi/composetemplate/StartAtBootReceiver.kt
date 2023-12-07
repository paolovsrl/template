package com.omsi.composetemplate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartAtBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val launchIntent = Intent(context, MainActivity::class.java)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(launchIntent)
    }

}