package com.ssafy.witch.ui.appointment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ssafy.witch.R

class MyLocationForegroundService : Service() {

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult : LocationResult) {
                val location = locationResult.lastLocation ?: return
                sendLocationToServer(location)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun sendLocationToServer(location : Location) {
        val latitude = location.latitude
        val longitude = location.latitude

        // TODO: 나의 위치 업데이트
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForegroundService()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val CHANNEL_ID = "notification_channel"
        val channel = NotificationChannel(
            CHANNEL_ID,
            "위치 전송 채널",
            NotificationManager.IMPORTANCE_LOW
        )
        manager.createNotificationChannel(channel)
        NotificationCompat.Builder(this, CHANNEL_ID)
    }

    private fun startForegroundService() {
        val notificationChannelId = "location_service_channel"
        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("약속 시간이 1시간도 채 남지 않았어요!")
            .setContentText("위치 전송 중..")
            .setSmallIcon(R.drawable.location)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setOngoing(true)
            .build()

        startForeground(101, notification)
    }

}