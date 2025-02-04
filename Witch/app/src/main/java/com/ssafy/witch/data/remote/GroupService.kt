package com.ssafy.witch.data.remote

import com.ssafy.witch.data.model.dto.FileName
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.response.CommonResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface GroupService {

    @POST("groups/{groupId}/profile-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : CommonResponse


    @POST("groups")
    suspend fun createGroup(
        @Body groupName: String,
        @Body groupImage: String
    ) : CommonResponse

    //edit, Todo api 확정 시 수정 필요
    @PATCH("groups/{groupId}/group-image")
    suspend fun editGroupImage(
        @Body objectKey: String
    ) : CommonResponse

    @PATCH("groups/{groupId}/group-name")
    suspend fun editGroupName(
        @Body groupName: String
    ) : CommonResponse




}