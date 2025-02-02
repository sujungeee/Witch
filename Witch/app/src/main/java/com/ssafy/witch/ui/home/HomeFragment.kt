package com.ssafy.witch.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.DateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.databinding.FragmentHomeBinding
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.group.GroupViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var calendarView: MaterialCalendarView

    private lateinit var homeListAdapter: HomeListAdapter


    private lateinit var schedule:MyAppointmentResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initAdapter()
        initView()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initView() {
        calendarView = binding.homeFgCvCalendar

        schedule = MyAppointmentResponse(listOf(
            MyAppointmentResponse.Appointment("1", "구미 인동 푸파", LocalDateTime.now(), "ACTIVE", "맛집탐방",),
            MyAppointmentResponse.Appointment("2", "윗치 api 명세 회의", LocalDateTime.of(2025,2,10,12,11), "PENDING", "D211",),
            MyAppointmentResponse.Appointment("3", "알고리즘 스터디", LocalDateTime.now(), "INACTIVE", "멋쟁이 알고리즘",)
        ))
        calendarView.selectedDate = CalendarDay.from(Date(System.currentTimeMillis()))

        getAppointmentList(CalendarDay.today())

        calendarView.setOnDateChangedListener { widget, date, selected ->
            getAppointmentList(date)
        }

        dotSchedule()
    }

    fun initAdapter(){
        homeListAdapter = HomeListAdapter(mutableListOf()){ position->
            (requireActivity() as MainActivity).openFragment(2)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentList(date: CalendarDay){
        binding.homeFgTvDate.text = "${date.year}-${date.month + 1}-${date.day}"
        binding.homeFgRvAppointment.adapter =
            HomeListAdapter(schedule.appointments.filter { it.appointmentTime.dayOfMonth == date.day && it.appointmentTime.monthValue-1==date.month }) { id ->
                (requireActivity() as MainActivity).openFragment(2)
            }
        // Todo : 서버에서 데이터 받아오면 바꾸기
        //            binding.homeFgRvAppointment.adapter =
//                viewModel.appointmentList.value?.let {
//                    HomeListAdapter(it.filter { it.appointmentTime.dayOfMonth == date.day }) { id ->
//                        (requireActivity() as MainActivity).openFragment(2)
//                    }
//                }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dotSchedule(){
        val eventDates = schedule.appointments.map { appointment ->
            CalendarDay.from(appointment.appointmentTime.year, appointment.appointmentTime.monthValue - 1,
                appointment.appointmentTime.dayOfMonth) }.toSet()

        val eventDecorator = EventDecorator(eventDates)
        calendarView.addDecorator(eventDecorator)

        calendarView.invalidateDecorators()
    }
}