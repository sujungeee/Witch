package com.ssafy.witch.ui.appointment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityAppointmentCreate3Binding
import com.ssafy.witch.ui.MainActivity

class AppointmentCreate3Activity : BaseActivity<ActivityAppointmentCreate3Binding>(
    ActivityAppointmentCreate3Binding::inflate){

    private var today: Long= 0
    private lateinit var minute: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        today = MaterialDatePicker.todayInUtcMilliseconds()

        binding.appointmentFgBtnNext.setOnClickListener {
            if (binding.appointmentFgTvDateChoice.text == "날짜를 선택하세요") {
                showCustomToast("날짜를 설정해주세요!")
            } else if (binding.appointmentFgTvTimeChoice.text == "시간을 선택하세요") {
                showCustomToast("시간을 설정해주세요!")
            } else {
                if (minute.toInt() % 10 == 0) {
                    setDialog()
                } else {
                    showCustomToast("10분 단위로 시간을 설정해주세요!")
                }
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            finish()
        }

        binding.appointmentFgTvDateChoice.setOnClickListener {
            showDatePickerDialog()
        }

        binding.appointmentFgIvDateClear.setOnClickListener {
            binding.appointmentFgTvDateChoice.text = "날짜를 선택하세요"
        }

        binding.appointmentFgTvTimeChoice.setOnClickListener {
            showTimePickerDialog()
        }

        binding.appointmentFgIvTimeClear.setOnClickListener {
            binding.appointmentFgTvTimeChoice.text = "시간을 선택하세요"
        }
    }

    private fun showTimePickerDialog() {
        val builder= MaterialTimePicker.Builder()
        builder.setTheme(R.style.CustomTimePickerDialog)
        builder.setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
        val picker= builder.build()


        picker.addOnPositiveButtonClickListener {
            var hour= String.format("%02d", picker.hour)
            minute= String.format("%02d", picker.minute)
            var state= "오전/오후"
            if (hour.toInt() >= 12) {
                state= "오후"
                hour= (hour.toInt() - 12).toString()
            } else {
                state= "오전"
            }
            binding.appointmentFgTvTimeChoice.text = state + " " + hour + "시 " + minute + "분"
        }
        picker.addOnNegativeButtonClickListener {

        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private fun showDatePickerDialog() {
        val builder= MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomDatePickerDialog)
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            binding.appointmentFgTvDateChoice.text = picker.headerText
        }
        picker.addOnNegativeButtonClickListener {

        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private fun setDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_create, null)
        val dialogBuilder = Dialog(this)
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.create()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        val appointmentCreateDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
        val appointmentCreateDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

        appointmentCreateDlBtnYes.setOnClickListener {
            // 약속 생성 가능 시 생성
            showCustomToast("약속이 생성되었어요!")
            val mainActivity = Intent(this, MainActivity::class.java)
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            mainActivity.putExtra("moveFragment", 6)
            startActivity(mainActivity)

            dialogBuilder.dismiss()
        }

        appointmentCreateDlBtnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}