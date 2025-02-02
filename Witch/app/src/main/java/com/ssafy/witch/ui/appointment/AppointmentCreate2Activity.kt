package com.ssafy.witch.ui.appointment

import android.content.Intent
import android.os.Bundle
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityAppointmentCreate2Binding
import com.ssafy.witch.ui.MainActivity

class AppointmentCreate2Activity : BaseActivity<ActivityAppointmentCreate2Binding>(
    ActivityAppointmentCreate2Binding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.appointmentFgTvAppointmentPlaceName.isSelected= true
        binding.appointmentFgTvAppointmentAddress.isSelected= true

        binding.appointmentFgBtnNext.setOnClickListener {
            val intent= Intent(this, AppointmentCreate3Activity::class.java)
            startActivity(intent)
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            finish()
        }

    }
}