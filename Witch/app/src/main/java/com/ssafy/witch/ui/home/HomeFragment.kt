package com.ssafy.witch.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.DayViewDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentHomeBinding
import java.util.Calendar


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {
    private var param1: String? = null
    private var param2: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView: MaterialCalendarView = binding.homeFgCvCalendar


        calendarView.setOnMonthChangedListener { widget, date ->
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            // 현재 달의 날짜는 흰색, 다른 달의 날짜는 회색으로 설정
            if (date.month == currentMonth) {
                // 현재 달 날짜에 흰색 텍스트 스타일 적용
                calendarView.setDateTextAppearance(R.style.CustomDateTextWhite)
            } else {
                // 다른 달 날짜에 회색 텍스트 스타일 적용
                calendarView.setDateTextAppearance(R.style.CustomDateTextGray)
            }
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}