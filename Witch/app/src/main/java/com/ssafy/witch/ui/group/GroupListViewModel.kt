package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import kotlinx.coroutines.launch

class GroupListViewModel : ViewModel() {
    private val _groupList = MutableLiveData<List<GroupListResponse.GroupListItem>>()
    val groupList: LiveData<List<GroupListResponse.GroupListItem>>
        get() = _groupList


    fun getGroupList(){
        viewModelScope.launch {
            runCatching {
                groupService.getMyGroupList()
            }.onSuccess {
                _groupList.value = it.data?.groups
            }.onFailure {
                it.printStackTrace()
            }
        }


    }



}