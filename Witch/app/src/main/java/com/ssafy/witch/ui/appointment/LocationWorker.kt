package com.ssafy.witch.ui.appointment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import java.time.Duration
import java.time.LocalDateTime

private const val TAG = "LocationWorker_Witch"
class LocationWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    @SuppressLint("MissingPermission")
    suspend override fun doWork(): Result {
        Log.d(TAG, "LocationWorker started")

        // 실시간 위치를 보내기 위한 약속 시간 확인
        runCatching {
            appointmentService.getMyAppointments(LocalDateTime.now().year, LocalDateTime.now().month.value)
        }.onSuccess {
            if (it.success == true) {
                Log.d(TAG, "doWork(): success")
                it.data?.appointments?.let { appointments ->
                    for (appointment in appointments) {
                        val remainingTime = Duration.between(LocalDateTime.now(), appointment.appointmentTime).toMinutes()

                        if (remainingTime in 0..30) {
                            scheduleAlarm(applicationContext, remainingTime)
                            return@onSuccess
                        }
                    }
                }
            } else {
                Log.d(TAG, "doWork(): fail")
            }
        }.onFailure {
            Log.d(TAG, "doWork(): ${it.message}")
        }

        return Result.success()
    }

    private fun scheduleAlarm(context: Context, remainderTime: Long) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyLocationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val triggerTime = System.currentTimeMillis() + (remainderTime * 60 * 1000)

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

}