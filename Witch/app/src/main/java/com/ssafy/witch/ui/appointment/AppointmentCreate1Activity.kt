package com.ssafy.witch.ui.appointment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityAppointmentCreate1Binding

class AppointmentCreate1Activity : BaseActivity<ActivityAppointmentCreate1Binding>(ActivityAppointmentCreate1Binding::inflate){

    private val appointmentViewModel: AppointmentViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (appointmentViewModel.title.value != null) {
            binding.appointmentFgEtName.setText(appointmentViewModel.title.value)
        }
        if (appointmentViewModel.summary.value != null) {
            binding.appointmentFgEtSummary.setText(appointmentViewModel.summary.value)
        }

        binding.appointmentFgBtnNext.setOnClickListener {
            if (binding.appointmentFgEtName.length() == 0){
                showCustomToast("약속 이름을 작성해주세요.")
            } else if (binding.appointmentFgEtName.length() > 30) {
                showCustomToast("약속 이름은 30자 이내여야 합니다.")
            } else if(binding.appointmentFgEtSummary.length() > 50) {
                showCustomToast("약속 요약은 50자 이내여야 합니다.")
            } else {
                appointmentViewModel.registerAppointment1(binding.appointmentFgEtName.text.toString(), binding.appointmentFgEtSummary.text.toString())
                val intent= Intent(this, AppointmentCreate2Activity::class.java)
                startActivity(intent)
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            finish()
        }

        val editText1 = binding.appointmentFgEtName
        editText1.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText1.compoundDrawablesRelative[2]

                drawableEnd?.let {
                    val iconStartX = editText1.width - editText1.paddingEnd - it.bounds.width()
                    if (event.x >= iconStartX) {
                        editText1.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val editText2 = binding.appointmentFgEtSummary
        editText2.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText2.compoundDrawablesRelative[2]

                drawableEnd?.let {
                    val iconStartX = editText2.width - editText2.paddingEnd - it.bounds.width()
                    if (event.x >= iconStartX) {
                        editText2.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }
}