package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {
    private val _tabState = MutableLiveData<String>()
    val tabState: LiveData<String>
        get() = _tabState


    fun setTabState(tabState: String) {
        _tabState.value = tabState
    }


}