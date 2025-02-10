package com.ssafy.witch.ui.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.dto.request.AppointmentRequest
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import kotlinx.coroutines.launch

private const val TAG = "AppointmentViewModel_Witch"
class AppointmentViewModel: ViewModel() {
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _appointmentList = MutableLiveData<MyAppointmentResponse?>()
    val appointmentList: LiveData<MyAppointmentResponse?>
        get() = _appointmentList

    private val _name= MutableLiveData<String>()
    val name: LiveData<String>
        get()= _name

    private val _summary= MutableLiveData<String>()
    val summary: LiveData<String>
        get()= _summary

    private val _appointmentTime= MutableLiveData<String>()
    val appointmentTime: LiveData<String>
        get()= _appointmentTime

    private val _latitude= MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get()= _latitude

    private val _longitude= MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get()= _longitude

    private val _address= MutableLiveData<String>()
    val address: LiveData<String>
        get()= _address

    fun appointmentClear() {
        _name.value = ""
        _summary.value = ""
        _appointmentTime.value = ""
        _latitude.value = Double.MIN_VALUE
        _longitude.value = Double.MIN_VALUE
        _address.value = ""
    }

    fun setName(name: String){
        _name.value = name
    }

    fun setSummary(summary: String){
        _summary.value = summary
    }

    fun setAppointmentTime(appointmentTime: String){
        _appointmentTime.value = appointmentTime
    }

    fun setLatitude(latitude: Double){
        _latitude.value = latitude
    }
    fun setLongitude(longitude: Double){
        _longitude.value = longitude
    }

    fun setAddress(address: String){
        _address.value = address
    }

    fun registerAppointment(){
        viewModelScope.launch {
            runCatching {
                appointmentService.registerAppointment( 1, //TODO: groupId
                    AppointmentRequest(
                        name.value!!,
                        summary.value!!,
                        appointmentTime.value!!,
                        latitude.value!!,
                        longitude.value!!,
                        address.value!!
                    )
                )
            }.onSuccess {
                if(it.success == true){
                    Log.d(TAG, "registerAppointment(): success")
                    _toastMsg.value = "약속이 생성되었어요!"
                    appointmentClear()
                } else {
                    Log.d(TAG, "registerAppointment(): fail")
                    _toastMsg.value = it.error.errorMessage
                }
            }.onFailure {
                Log.d(TAG, "registerAppointment(): ${it.message}")
                it.printStackTrace()
            }
        }
    }

    fun deleteAppointment(appointmentId: Int) {
        viewModelScope.launch {
            runCatching {
                appointmentService.deleteAppointment(appointmentId)
            }.onSuccess {
                if (it.success == true) {
                    Log.d(TAG, "deleteAppointment(): success")
                    _toastMsg.value = "약속이 삭제되었어요!"
                } else {
                    Log.d(TAG, "deleteAppointment(): fail")
                    _toastMsg.value = it.error.errorMessage
                }
            }.onFailure {
                Log.d(TAG, "deleteAppointment(): ${it.message}")
                it.printStackTrace()
            }
        }
    }

    fun participateAppointment(appointmentId: Int) {
        viewModelScope.launch {
            runCatching {
                appointmentService.participateAppointment(appointmentId)
            }.onSuccess {
                if(it.success == true) {
                    Log.d(TAG, "participateAppointment(): success")
                    _toastMsg.value = "약속에 참여했어요!"
                } else {
                    Log.d(TAG, "participateAppointment(): fail")
                    _toastMsg.value = it.error.errorMessage
                }
            }.onFailure {
                Log.d(TAG, "participateAppointment(): fail")
                _toastMsg.value = it.message
            }
        }
    }

    fun leaveAppointment(appointmentId: Int) {
        viewModelScope.launch {
            runCatching {
                appointmentService.leaveAppointment(appointmentId)
            }.onSuccess {
                if (it.success == true) {
                    Log.d(TAG, "leaveAppointment(): success")
                    _toastMsg.value = "약속에서 탈퇴했어요!"
                } else {
                    Log.d(TAG, "leaveAppointment(): fail")
                    _toastMsg.value = it.error.errorMessage
                }
            }.onFailure {
                Log.d(TAG, "leaveAppointment: ${it.message}")
                it.printStackTrace()
            }
        }
    }
}