package com.ssafy.witch.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ssafy.witch.data.model.dto.JoinUser
import com.ssafy.witch.data.model.dto.User
import java.time.LocalDateTime

// 유저 정보 로컬에 저장하면 루팅시 평문상태로 개인정보 접근 가능하여 보안상 취약점 발생
// 로컬에선 비밀번호 저장 저장 금지, JWT 로 접근하기
// 단순 sharedPreferences는 평문 저장, EncryptedSharedPreferences 로 AES-256 암호화 보호
class SharedPreferencesUtil (context : Context) {
    private val SHARED_PREFERENCES_NAME = "witch_secure_preference"
//    val COOKIES_KEY_NAME = "cookies"

    // 서버에서 email과 password 로 로그인 성공시 보안을 위해 JWT로 토큰값 저장
    // JWT 토큰 키값
    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"

    private val KEY_EMAIL = "email"
    private val KEY_NICK = "nickname"

    //블랙리스트 구현 필요?

    // AES-256 암호화된 SharedPreferences 생성
    //// security-crypto 라이브러리 APP 단위 빌드그레이들 플러그인 추가
    private var preference: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preference = EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    //JWT 토큰 저장 - JWT 방식이면 쿠키 필요 없음
    //Access Token이 만료되면 Refresh Token을 이용하여 재발급하면 자동 로그인 기능을 구현
    fun saveToken(accessToken: String, refreshToken: String) {
        preference.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    //JWT Access Token 가져오기
    fun getAccessToken(): String? {
        return preference.getString(KEY_ACCESS_TOKEN, null)
    }

    //JWT Refresh Token 가져오기
    fun getRefreshToken(): String? {
        return preference.getString(KEY_ACCESS_TOKEN, null)
    }

    //로그아웃 시 모든 JWT 토큰 삭제
    fun clearToken() {
        preference.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_REFRESH_TOKEN).apply()
    }

    //사용자 정보 저장, user dto 생성하기
    fun addUser(user: JoinUser) {
        val editor =  preference.edit()
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_NICK, user.nickname)
        editor.apply()
    }

    fun getUser(): JoinUser {
        val email = preference.getString(KEY_EMAIL, "")
        if(email != "") {
            val nick = preference.getString(KEY_NICK, "")
            return JoinUser("", email!!, nick!!, "")
        } else{
          return JoinUser()
        }
    }

    fun deleteUser(){
        //preference 지우기
        val editor = preference.edit()
        editor.clear()
        editor.apply()
    }



}