package com.ssafy.witch.ui.appointment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyLocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, MyLocationForegroundService::class.java)
        context.startForegroundService(serviceIntent)
    }

}