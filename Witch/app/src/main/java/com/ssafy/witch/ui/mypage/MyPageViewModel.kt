package com.ssafy.witch.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.model.dto.EditPwd
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user


    private val _appointmentList = MutableLiveData<MyAppointmentResponse>()
    val appointmentList: LiveData<MyAppointmentResponse>
        get() = _appointmentList

    fun setName(name: String) {
        _name.value = name
    }

    fun editPassword(password: String, newPassword: String, context:ContentActivity) {
        viewModelScope.launch {
            runCatching {
                userService.editPassword(EditPwd(password, newPassword))
            }.onSuccess {
                context.finish()
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
    fun getProfile(){
        viewModelScope.launch {
            runCatching {
                userService.getProfile()
            }.onSuccess {
                if (it.isSuccessful) {
                    it.body()?.data?.let { user ->
                        try {
                            _user.value = user
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