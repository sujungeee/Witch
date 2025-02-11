package com.ssafy.witch.ui.appointment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

private const val TAG = "MapViewModel_Witch"
class MapViewModel : ViewModel(){
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _remainderTime = MutableLiveData<Long?>()
    val remainderTime: LiveData<Long?>
        get() = _remainderTime

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get() = _longitude

    fun setRemainderTime(time: Long?) {
        _remainderTime.value = time
    }
}