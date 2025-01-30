package com.ssafy.witch.data.remote

import com.ssafy.witch.base.ApplicationClass

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserService::class.java)

    }
}