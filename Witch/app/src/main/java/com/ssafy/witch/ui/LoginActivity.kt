package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.databinding.ActivityLoginBinding
import com.ssafy.witch.login.JoinFragment
import com.ssafy.witch.login.LoginFragment
import com.ssafy.witch.login.LoginFragmentViewModel
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var loginViewModel: LoginFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = SharedPreferencesUtil(applicationContext)

        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        lifecycleScope.launch {
            if (isUserLoggedIn()) {
                openFragment(1)
            } else {
                //Refresh Token도 만료되었다면 로그인 화면으로 이동
                moveToLogin()
            }
        }
    }

    //로그인 화면으로 이동
    private fun moveToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_a_fl, LoginFragment())
            .commit()
    }

    private fun isUserLoggedIn(): Boolean {
        val accessTokenExpiresAt = sharedPreferences.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = sharedPreferences.getRefreshTokenExpiresAt()
        return accessTokenExpiresAt > getCurrentTime() || refreshTokenExpiresAt > getCurrentTime()
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis() / 1000
    }

    // 로그인 시 해당하는 번호의 프래그먼트 지정
    fun openFragment(int: Int) {
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
                transaction.replace(R.id.login_a_fl, LoginFragment())
            }
        }
        transaction.commit()
    }

}