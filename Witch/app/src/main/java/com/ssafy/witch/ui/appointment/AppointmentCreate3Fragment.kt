package com.ssafy.witch.ui.appointment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentAppointmentCreate3Binding
import com.ssafy.witch.ui.MainActivity

class AppointmentCreate3Fragment : BaseFragment<FragmentAppointmentCreate3Binding>(
    FragmentAppointmentCreate3Binding::bind, R.layout.fragment_appointment_create3) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appointmentFgBtnNext.setOnClickListener {
            setDialog()
        }
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
            // 약속 생성 가능 시 생성

            showCustomToast("약속이 생성되었어요!")
            (requireActivity() as MainActivity).openFragment(7)
            dialogBuilder.dismiss()
        }

        appointmentCreateDlBtnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}