package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.dto.GroupListItem
import com.ssafy.witch.databinding.FragmentGroupBinding
import com.ssafy.witch.databinding.FragmentGroupListBinding
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.appointment.AppointmentCreate1Activity
import java.time.LocalDateTime


class GroupFragment : BaseFragment<FragmentGroupBinding>(FragmentGroupBinding::bind, R.layout.fragment_group) {
    private lateinit var appointmentListAdapter: AppointmentListAdapter
    private lateinit var appointmentList: List<AppointmentListItem>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.groupFgIvAppointmentCreate.setOnClickListener {
            val intent= Intent(requireContext(), AppointmentCreate1Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    fun initAdapter(){
        appointmentList = listOf(
            AppointmentListItem(1, "알고리즘 스터디", LocalDateTime.now(), "APROVED"),
            AppointmentListItem(2, "맛집탐방",  LocalDateTime.now(), "APROVED"),
            AppointmentListItem(3, "멋쟁이 알고리즘",  LocalDateTime.now(), "INACTIVE"),
            AppointmentListItem(4, "알고리즘",  LocalDateTime.now(), "INACTIVE"),
            AppointmentListItem(5, "멋쟁이",  LocalDateTime.now(), "APROVED")
        )
        binding.groupFgRvAppointmentList.adapter = AppointmentListAdapter(appointmentList) { id ->
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, GroupFragment()).commit()
        }
    }

}