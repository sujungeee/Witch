package com.ssafy.witch.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.GroupResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import kotlinx.coroutines.launch

class GroupApprovalViewModel : ViewModel() {
    private val _group = MutableLiveData<GroupResponse>()
    val group: LiveData<GroupResponse>
        get() = _group


    fun getGroupPreview(groupId: String) {
        viewModelScope.launch {
            runCatching {
                groupService.getGroupPreview(groupId)
            }.onSuccess {
                _group.value = it.body()?.data ?: GroupResponse("","","")
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun requestJoinGroup(groupId: String) {
        viewModelScope.launch {
            try {
                groupService.requestJoinGroup(groupId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}