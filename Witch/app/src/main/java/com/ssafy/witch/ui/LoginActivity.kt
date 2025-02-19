package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.databinding.ActivityLoginBinding
import com.ssafy.witch.ui.auth.JoinFragment
import com.ssafy.witch.ui.auth.LoginFragment
import com.ssafy.witch.ui.auth.LoginFragmentViewModel

private const val TAG = "LoginActivity"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var loginViewModel: LoginFragmentViewModel

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Witch)
        sharedPreferences = SharedPreferencesUtil(applicationContext)
        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        id = intent.getIntExtra("state", 0)
        checkTokenValidity()
    }

    private fun checkTokenValidity() {
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        val storedAccessToken = sharedPref.getAccessToken()
        val accessTokenExpiresAt = sharedPref.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()
        val storedRefreshToken = sharedPref.getRefreshToken()
        Log.d(TAG, "🔹 저장된 Refresh Token: $storedRefreshToken")
        val refreshTokenIssuedAt = sharedPref.getRefreshTokenRenewAvailableSeconds()
        val currentTime = System.currentTimeMillis() / 1000

        Log.d(TAG, "현재 시간: $currentTime")
        Log.d(TAG, "AccessToken 만료 시간: $accessTokenExpiresAt")
        Log.d(TAG, "RefreshToken 만료 시간: $refreshTokenExpiresAt")
        Log.d(TAG, "RefreshToken 갱신 가능 시간: $refreshTokenIssuedAt")

        // Refresh Token 만료 확인 (7일 기준)
        if (currentTime > refreshTokenExpiresAt) {
            Log.d(TAG, "Refresh Token 만료됨. 로그인 필요.")
            clearTokenLogin()
            return
        }

        // Refresh Token 갱신 가능 여부 확인 (5일 이후)
        val canRenew = (refreshTokenIssuedAt < currentTime) && (currentTime < refreshTokenExpiresAt)
        if (canRenew) {
            loginViewModel.renewRefreshToken { success ->
                if (!success) {
                    Log.d(TAG, "❌ Refresh Token 갱신 실패. 로그인 필요.")
                    clearTokenLogin()
                } else {
                    val newAccessTokenExpiresAt = sharedPref.getAccessTokenExpiresAt()
                    val newRefreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()
                    val newRefreshTokenRenewAvailableSeconds =
                        sharedPref.getRefreshTokenRenewAvailableSeconds()

                    Log.d(TAG, "✅ Refresh Token 재갱신 성공.")
                    Log.d(TAG, "현재 시간: $currentTime")
                    Log.d(TAG, "AccessToken 만료 시간 재갱신: $newAccessTokenExpiresAt")
                    Log.d(TAG, "RefreshToken 만료 시간 재갱신: $newRefreshTokenExpiresAt")
                    Log.d(TAG, "RefreshToken 갱신 가능 시간 재갱신 : $newRefreshTokenRenewAvailableSeconds")

//                    openFragment(1)
                }

            }
        } else {
            Log.d(TAG, "Refresh Token 갱신 조건 미충족 (5일 미만)")
            //액세스토큰 시간 만료시 갱신 여기서 하기.
            if (currentTime > accessTokenExpiresAt) {
                loginViewModel.reissueAccessToken { success ->
                    if (success) {
                        Log.d(TAG, "✅ 액세스 토큰 재갱신 성공 → 최신 토큰 반영 후 API 재시도")

                        // 최신 토큰 반영
                        val newAccessToken = sharedPreferencesUtil.getAccessToken()
                        Log.d(TAG, "🔹 최신 액세스 토큰 확인: $newAccessToken")

                    } else {
                        Log.d(TAG, "❌ 액세스 토큰 재갱신 실패 → 강제 로그아웃")
                        clearTokenLogin()
                    }
                }
            }
        }

        openFragment(1, id)

    }

    //로그인 액티비티 이동 함수
    private fun clearTokenLogin() {
        //자동 로그아웃 시 토큰 다 날리기
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()

        if (refreshTokenExpiresAt.toInt() != 0) {
            showToast("로그인 정보가 만료되었습니다.")
        }
        sharedPref.clearToken()
        openFragment(3,id)
    }

    //토스트 메시지 출력 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 로그인 시 해당하는 번호의 프래그먼트 지정
    fun openFragment(int: Int, id: Int = 0) {
        val transaction = supportFragmentManager.beginTransaction()
        when(int) {
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            2 -> transaction.replace(R.id.login_a_fl, JoinFragment())
                .addToBackStack(null)
            3 -> {
                // 회원가입한 뒤 돌아오면, 2번에서 addToBackStack해 놓은게 남아 있어서,
                // stack을 날려 줘야 한다. stack날리기.
                supportFragmentManager.popBackStack()
                transaction.replace(R.id.login_a_fl, LoginFragment.newInstance("state",id))
            }
        }
        transaction.commit()
    }

}