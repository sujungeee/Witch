package com.ssafy.witch.ui.appointment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentAppointmentCreate3Binding
import com.ssafy.witch.ui.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "AppointmentCreate3Fragment_Witch"
class AppointmentCreate3Fragment : BaseFragment<FragmentAppointmentCreate3Binding>(
    FragmentAppointmentCreate3Binding::bind, R.layout.fragment_appointment_create3){

    private var today: Long= 0
    private lateinit var minute: String
    private lateinit var hour: String
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        today = MaterialDatePicker.todayInUtcMilliseconds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appointmentFgBtnNext.setOnClickListener {
            if (binding.appointmentFgTvDateChoice.text == "날짜를 선택하세요") {
                showCustomToast("날짜를 설정해주세요!")
            } else if (binding.appointmentFgTvTimeChoice.text == "시간을 선택하세요") {
                showCustomToast("시간을 설정해주세요!")
            } else {
                setDialog()
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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

    private fun changeLocalDateTime(): LocalDateTime{
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
        val localDate = java.time.LocalDate.parse(binding.appointmentFgTvDateChoice.text, dateFormatter)

        val hour = hour.toInt()
        val minute = minute.toInt()

        return LocalDateTime.of(localDate, java.time.LocalTime.of(hour, minute))
    }

    private fun showTimePickerDialog() {
        val builder= MaterialTimePicker.Builder()
        builder.setTheme(R.style.CustomTimePickerDialog)
        builder.setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
        val picker= builder.build()

        picker.addOnPositiveButtonClickListener {
            hour= String.format("%02d", picker.hour)
            minute= String.format("%02d", picker.minute)
            var state= "오전/오후"
            if (hour.toInt() >= 12) {
                state= "오후"
                binding.appointmentFgTvTimeChoice.text = state + " " + (hour.toInt() - 12).toString() + "시 " + minute + "분"
            } else {
                state= "오전"
                binding.appointmentFgTvTimeChoice.text = state + " " + hour + "시 " + minute + "분"
            }
        }
        picker.addOnNegativeButtonClickListener {

        }

        picker.show(childFragmentManager, picker.toString())
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

        picker.show(childFragmentManager, picker.toString())
    }

    private fun setDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_create, null)
        val dialogBuilder = Dialog(requireContext())
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.create()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        val appointmentCreateDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
        val appointmentCreateDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

        appointmentCreateDlBtnYes.setOnClickListener {
            appointmentViewModel.setAppointmentTime(changeLocalDateTime().toString())
            appointmentViewModel.registerAppointment()
            appointmentViewModel.toastMsg.observe(viewLifecycleOwner) { msg ->
                showCustomToast(msg)
                if (msg == "약속이 생성되었어요!") {
                    requireActivity().finish()
                    dialogBuilder.dismiss()
                }
            }
        }

        appointmentCreateDlBtnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}