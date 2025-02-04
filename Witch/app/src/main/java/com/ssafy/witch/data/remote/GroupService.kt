package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface GroupService {

    @POST("groups/{groupId}/profile-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : BaseResponse<PresignedUrl>


    @POST("groups")
    suspend fun createGroup(
        @Body groupName: String,
        @Body groupImage: String
    ) : BaseResponse<String>

    //edit, Todo api 확정 시 수정 필요
    @PATCH("groups/{groupId}/group-image")
    suspend fun editGroupImage(
        @Body objectKey: String
    ) : BaseResponse<String>

    @PATCH("groups/{groupId}/group-name")
    suspend fun editGroupName(
        @Body groupName: String
    ) : BaseResponse<String>




}