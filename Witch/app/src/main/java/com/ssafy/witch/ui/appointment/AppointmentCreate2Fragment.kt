package com.ssafy.witch.ui.appointment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentAppointmentCreate2Binding
import com.ssafy.witch.ui.ContentActivity

class AppointmentCreate2Fragment : BaseFragment<FragmentAppointmentCreate2Binding>(
    FragmentAppointmentCreate2Binding::bind, R.layout.fragment_appointment_create2){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appointmentFgTvAppointmentPlaceName.isSelected= true
        binding.appointmentFgTvAppointmentAddress.isSelected= true

        binding.appointmentFgBtnNext.setOnClickListener {
            (requireActivity() as ContentActivity).openFragment(8)
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}