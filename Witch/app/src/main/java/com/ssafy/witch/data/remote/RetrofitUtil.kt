package com.ssafy.witch.data.remote

import com.ssafy.witch.base.ApplicationClass

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserService::class.java)

        val appointmentService = ApplicationClass.retrofit.create(AppointmentService::class.java)

        val authService = ApplicationClass.retrofit.create(AuthService::class.java)

        val joinService = ApplicationClass.retrofit.create(JoinService::class.java)

        val groupService = ApplicationClass.retrofit.create(GroupService::class.java)

    }
}