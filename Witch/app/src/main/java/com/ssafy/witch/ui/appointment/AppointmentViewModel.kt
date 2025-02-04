package com.ssafy.witch.ui.appointment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime

class AppointmentViewModel: ViewModel() {
    private val _title= MutableLiveData<String>()
    val title: LiveData<String>
        get()= _title

    private val _summary= MutableLiveData<String?>()
    val summary: LiveData<String?>
        get()= _summary

    private val _appointmentTime= MutableLiveData<LocalDateTime>()
    val appointmentTime: LiveData<LocalDateTime>
        get()= _appointmentTime

    private val _latitude= MutableLiveData<BigDecimal>()
    val latitude: LiveData<BigDecimal>
        get()= _latitude

    private val _longitude= MutableLiveData<BigDecimal>()
    val longitude: LiveData<BigDecimal>
        get()= _longitude

    private val _address= MutableLiveData<String>()
    val address: LiveData<String>
        get()= _address

    fun appointmentClear(title: String, summary: String?, appointmentTime: LocalDateTime, latitude: BigDecimal, longitude: BigDecimal, address: String) {
        viewModelScope.launch {
            try {
                _title.value = ""
                _summary.value = null
                _appointmentTime.value = LocalDateTime.now()
                _latitude.value = BigDecimal.ZERO
                _longitude.value = BigDecimal.ZERO
                _address.value = ""
            } catch (e: Exception) {
                Log.e("appointmentClear(AppointmentViewModel)", e.message.toString())
            }
        }
    }

    fun registerAppointment1(title: String, summary: String?){
        viewModelScope.launch{
            try {
                _title.value = title
                _summary?.value= summary
            } catch (e: Exception){
                Log.e("registerAppointment1(AppointmentViewModel)", e.message.toString())
            }
        }
    }

    fun registerAppointment2(longitude: BigDecimal, latitude: BigDecimal, address: String){
        viewModelScope.launch{
            try {
                _longitude.value = longitude
                _latitude.value = latitude
                _address.value = address
            } catch(e: Exception){
                Log.e("registerAppointment2(AppointmentViewModel)", e.message.toString())
            }
        }
    }

    fun registerAppointment3(appointmentTime: LocalDateTime){
        viewModelScope.launch {
            try {
                _appointmentTime.value = appointmentTime
            } catch (e: Exception) {
                Log.e("registerAppointment3(AppointmentViewModel)", e.message.toString())
            }
        }
    }
}