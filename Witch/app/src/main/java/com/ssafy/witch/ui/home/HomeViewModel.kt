package com.ssafy.witch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name


    private val _appointmentList = MutableLiveData<MyAppointmentResponse>()
    val appointmentList: LiveData<MyAppointmentResponse>
        get() = _appointmentList

    fun setName(name: String) {
        _name.value = name
    }


    fun getAllAppointments(year: Int, month: Int) {
        var appointmentLists: MutableList<MyAppointment> = mutableListOf()

        viewModelScope.launch {
            runCatching {
                appointmentService.getMyAppointments(year,month)
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


    fun getProfile(){
        viewModelScope.launch {
            runCatching {
                userService.getProfile()
            }.onSuccess {
                if (it.success) {
                    it.data?.let { user ->
                        try {
                            ApplicationClass.sharedPreferencesUtil.addUser(user)
                            _name.value = user.nickname
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