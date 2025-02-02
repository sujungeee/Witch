package com.ssafy.witch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _appointmentList = MutableLiveData<List<MyAppointmentResponse>>()
    val appointmentList: LiveData<List<MyAppointmentResponse>>
        get() = _appointmentList



    fun getAllAppointments() {
        val appointmentLists: MutableList<MyAppointmentResponse> = mutableListOf()

        viewModelScope.launch {
            runCatching {
                appointmentService.getMyAppointments()
            }.onSuccess {
                if (it.success) {
                    it.data?.let { appointments ->
                        try {
                            val gson = com.google.gson.Gson()
                            val appointments: List<MyAppointmentResponse> = gson.fromJson(appointments, Array<MyAppointmentResponse>::class.java).toList()

                            appointmentLists.addAll(appointments)

                            _appointmentList.value = appointmentLists

                        } catch (e: Exception) {
                            // 변환 중 오류 발생 시 처리
                            e.printStackTrace()
                        }
                    }
                } else {
                    // 실패
                }
            }.onFailure {

            }
        }

    }


}