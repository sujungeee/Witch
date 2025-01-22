package com.ssafy.witch.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentHomeBinding
import com.ssafy.witch.ui.MainActivity
import java.util.Calendar

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView: MaterialCalendarView = binding.homeFgCvCalendar

        calendarView.setOnMonthChangedListener { widget, date ->
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            if (date.month == currentMonth) {
                calendarView.setDateTextAppearance(R.style.CustomDateTextWhite)
            } else {
                calendarView.setDateTextAppearance(R.style.CustomDateTextGray)
            }
        }

        binding.textView2.setOnClickListener {
            (requireActivity() as MainActivity).openFragment(4)
        }
    }
}