package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {
    private val _tabState = MutableLiveData<String>("MEMBER")
    val tabState: LiveData<String>
        get() = _tabState


    fun setTabState(tabState: String) {
        _tabState.value = tabState
    }

    fun groupOut(){
        // 그룹 탈퇴
    }

    fun deleteGroup(){
        // 그룹 삭제
    }


}