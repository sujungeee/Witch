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
import java.time.LocalDateTime

class HomeViewModel : ViewModel() {
    private val _appointmentList = MutableLiveData<MyAppointmentResponse>()
    val appointmentList: LiveData<MyAppointmentResponse>
        get() = _appointmentList



    fun getAllAppointments() {
        var appointmentLists: MutableList<MyAppointmentResponse> = mutableListOf()

        viewModelScope.launch {
            runCatching {
                appointmentService.getMyAppointments(LocalDateTime.now().year, LocalDateTime.now().month.value)
            }.onSuccess {
                if (it.success) {
                    it.data?.let { appointments ->
                        try {
                            _appointmentList.value = appointments
                        } catch (e: Exception) {
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