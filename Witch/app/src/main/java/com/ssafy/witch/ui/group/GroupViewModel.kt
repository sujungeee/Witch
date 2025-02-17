package com.ssafy.witch.ui.group

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.GroupJoinListResponse.JoinRequest
import com.ssafy.witch.data.model.response.GroupMemberResponse
import com.ssafy.witch.data.model.response.GroupResponse
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {
    private val _tabState = MutableLiveData<String>("MEMBER")
    val tabState: LiveData<String>
        get() = _tabState


    private val _group = MutableLiveData<GroupResponse>()
    val group: LiveData<GroupResponse>
        get() = _group

    private val _groupMember = MutableLiveData<List<User>>()
    val groupMember: LiveData<List<User>>
        get() = _groupMember


    private val _groupJoinList = MutableLiveData<List<JoinRequest>>()
    val groupJoinList: LiveData<List<JoinRequest>>
        get() = _groupJoinList


    private val _groupAppointments = MutableLiveData<List<MyAppointment>>()
    val groupAppointments: LiveData<List<MyAppointment>>
        get() = _groupAppointments



    fun setTabState(tabState: String) {
        _tabState.value = tabState
    }

    fun setGroup(group: GroupResponse) {
        _group.value = group
    }

    fun setGroupMember(members: List<User>) {
        _groupMember.value = members
    }

    fun setGroupJoinList(joinMembers: List<JoinRequest>) {
        _groupJoinList.value = joinMembers
    }

    fun getGroup(groupId: String){
        viewModelScope.launch {
            runCatching {
                groupService.getGroup(groupId)
            }.onSuccess {
                _group.value = it.data ?: GroupResponse(0, "", "", false, "")
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun groupOut(groupId: String, context: MainActivity){
        viewModelScope.launch {
            runCatching {
                groupService.leaveGroup(groupId)
            }.onSuccess {
                context.openFragment(2)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun deleteGroup(groupId: String, context: MainActivity){
        // 그룹 삭제
        viewModelScope.launch {
            runCatching {
//                groupService.deleteGroup(groupId)
            }.onSuccess {
                context.openFragment(2)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getGroupMemberList(groupId: String){
        // 그룹 멤버 리스트
        viewModelScope.launch {
            runCatching {
                groupService.getGroupMembers(groupId)
            }.onSuccess {
                // 멤버 리스트 받아오기
                _groupMember.value = it.data?.members
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getGroupJoinList(groupId: String){
        // 그룹 가입 신청자 리스트
        viewModelScope.launch {
            runCatching {
                groupService.getJoinRequests(groupId)
            }.onSuccess {
                // 가입 신청자 리스트 받아오기
                _groupJoinList.value = it.data?.joinRequests
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun approveJoinRequest(joinRequestId: String){
        // 가입 신청자 수락
        viewModelScope.launch {
            runCatching {
                groupService.approveJoinRequest(joinRequestId)
            }.onSuccess {
                // 가입 신청자 수락 성공
                _groupJoinList.value = _groupJoinList.value?.filter { it.joinRequestId != joinRequestId }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun rejectJoinRequest(joinRequestId: String){
        // 가입 신청자 거절
        viewModelScope.launch {
            runCatching {
                groupService.rejectJoinRequest(joinRequestId)
            }.onSuccess {
                // 가입 신청자 거절 성공
                _groupJoinList.value = _groupJoinList.value?.filter { it.joinRequestId != joinRequestId }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getGroupAppointments(groupId: String){
        // 그룹 일정 리스트
        viewModelScope.launch {
            runCatching {
                appointmentService.getGroupAppointments(groupId)
            }.onSuccess {
                // 일정 리스트 받아오기
                _groupAppointments.value = it.data?.appointments
            }.onFailure {
                it.printStackTrace()
            }
        }
    }


}