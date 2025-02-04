package com.ssafy.witch.ui.snack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import kotlinx.coroutines.launch
import java.io.File

class SnackCreateViewModel : ViewModel() {
    private val _snackText = MutableLiveData<String>()
    val snackText: LiveData<String>
        get() = _snackText


    private val _photoFile = MutableLiveData<File>()
    val photoFile: LiveData<File>
        get() = _photoFile


    private val _audioFile = MutableLiveData<File>()
    val audioFile: LiveData<File>
        get() = _audioFile


}