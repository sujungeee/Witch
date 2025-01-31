package com.ssafy.witch.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.HomeAppointment
import com.ssafy.witch.databinding.FragmentHomeBinding
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    private lateinit var homeListAdapter: HomeListAdapter
    private lateinit var schedule:List<HomeAppointment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initAdapter()
        val calendarView: MaterialCalendarView = binding.homeFgCvCalendar
        schedule= listOf(
            HomeAppointment(1,"맛집탐방", LocalDateTime.now(), "구미 인동 푸파"),
            HomeAppointment(2,"D211", LocalDateTime.now(), "윗치 api 명세 회의"),
            HomeAppointment(3,"멋쟁이 알고리즘", LocalDateTime.now(), "알고리즘 스터디"),
        )
        calendarView.selectedDate= CalendarDay.from(Date(System.currentTimeMillis()))


        calendarView.setOnDateChangedListener { widget, date, selected ->
            binding.homeFgTvDate.text = "${date.year}-${date.month+1}-${date.day}"
            binding.homeFgRvAppointment.adapter = HomeListAdapter(schedule.filter { it.appointment_time.dayOfMonth == date.day }) { id ->
                    (requireActivity() as MainActivity).openFragment(2)
                }
        }

        binding.textView2.setOnClickListener {
//            val intent= Intent(activity, AppointmentCreate1Fragment::class.java)
//            startActivity(intent)
            (requireActivity() as MainActivity).openFragment(7)
        }

    }

    fun initAdapter(){
        homeListAdapter = HomeListAdapter(mutableListOf()){ position->
            (requireActivity() as MainActivity).openFragment(2)
        }


        binding.homeFgRvAppointment.apply {
            lifecycleScope.launch {
                //Todo 뷰모델 만들면 observer 추가
            }
        }
    }
}