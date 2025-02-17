package com.ssafy.witch.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ssafy.witch.R

private const val TAG = "ForegroundService_Witch"
class ForegroundService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val type = intent?.getStringExtra("type") ?: "default"
        val id = intent?.getStringExtra("id") ?: "아이디"
        val parameter = intent?.getStringExtra("parameter") ?: "파라미터"
        val title = intent?.getStringExtra("title") ?: "알림 제목"
        val content = intent?.getStringExtra("body") ?: "알림 내용"

        val channelId = getChannelId(type)
        createNotificationChannel(channelId, type)
        startForegroundService(channelId, type, id, parameter, title, content)

        return START_STICKY
    }

    private fun startForegroundService(channelId: String, type: String, id: String, parameter: String, title: String, content: String) {
        val pendingIntent = createPendingIntent(type, id, parameter)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.location)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET) // 알림 안뜨게
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(101, notification)
    }

    private fun createNotificationChannel(channelId: String, type: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(NotificationChannel(channelId, getChannelName(type), importance))
    }

    private fun getChannelId(type: String): String {
        return when (type) {
            "appointment" -> "appointment_channel"
            "group" -> "group_channel"
            else -> "default_channel"
        }
    }

    private fun getChannelName(type: String): String {
        return when (type) {
            "appointment" -> "약속 알림"
            "group" -> "모임 알림"
            else -> "기본 알림"
        }
    }

    private fun createPendingIntent(type: String, id: String, parameter: String): PendingIntent {
        val intent = when (type) {
            "group" -> Intent(this, MainActivity::class.java).apply {
                putExtra("openFragment", 5)
                putExtra("id", parameter)
            }
            "appointment" -> Intent(this, ContentActivity::class.java).apply {
                putExtra("openFragment", 9)
                putExtra("id", parameter)
            }
            else -> Intent(this, LoginActivity::class.java).apply {
                putExtra("openFragment", 1)
            }
        }

        return PendingIntent.getActivity(this, 10101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

}
