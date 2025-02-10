package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.auth.LoginFragmentViewModel
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.mypage.MyPageFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    // 메인액티비티 자동로그인 후 토큰만료시간에 따른 토큰 재발급 및 자동 로그아웃 로직 위한 import
    private lateinit var loginViewModel: LoginFragmentViewModel

    private val mainBinding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        // ViewModel 초기화 (토큰 재발급 함수 사용)
        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        // 앱 시작 시 또는 액티비티 진입 시 토큰 유효성 체크
        checkTokenValidity()

        val fragmentIdx = intent.getIntExtra("moveFragment", -1)
        if (fragmentIdx != -1) {
            moveFragment(fragmentIdx)
            intent.removeExtra("moveFragment")
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_flayout, HomeFragment())
                .commit()
        }

        mainBinding.mainABn.selectedItemId = R.id.bn_home

        mainBinding.mainABn.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bn_home -> {
                    openFragment(1)
                    true
                }
                R.id.bn_group -> {
                    openFragment(2)
                    true
                }
                R.id.bn_mypage -> {
                    openFragment(3)
                    true
                }
                else -> false
            }

        }
        mainBinding.mainABn.setOnItemReselectedListener { item ->
            if(mainBinding.mainABn.selectedItemId != item.itemId){
                mainBinding.mainABn.selectedItemId = item.itemId
            }
        }

    }

    fun openFragment(index: Int, id: String = "") {
        moveFragment(index, id)
    }

    private fun moveFragment(index:Int, id: String = "") {
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> transaction.replace(R.id.main_flayout, HomeFragment())
            2 -> transaction.replace(R.id.main_flayout, GroupListFragment())
            3 -> transaction.replace(R.id.main_flayout, MyPageFragment())
            5 -> transaction.replace(R.id.main_flayout, GroupFragment.newInstance("groupId", id))
                .addToBackStack(null)
            6 -> transaction.replace(R.id.main_flayout, GroupCreateFragment())
                .addToBackStack(null)

            7 -> transaction.replace(R.id.main_flayout, GroupEditFragment())
                .addToBackStack(null)

            8 -> transaction.replace(R.id.main_flayout, SnackCreateFragment())
        }
        transaction.commit()
    }

    /**
     * 토큰 유효성을 확인하여, 액세스 토큰이 만료된 경우 리프레시 토큰으로 재발급 시도,
     * 리프레시 토큰도 만료된 경우 로그인 화면으로 전환.
     */
    private fun checkTokenValidity() {
        val sharedPref = ApplicationClass.sharedPreferencesUtil
        val accessTokenExpiresAt = sharedPref.getAccessTokenExpiresAt() ?: 0L
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt() ?: 0L
        val currentTime = System.currentTimeMillis() / 1000

        when {
            // 액세스 토큰이 유효한 경우: 아무런 조치 없이 진행
            accessTokenExpiresAt > currentTime -> {
                // 토큰이 유효합니다.
            }
            // 액세스 토큰은 만료되었으나, refresh token은 유효한 경우
            refreshTokenExpiresAt > currentTime -> {
                // 우선 액세스 토큰 재발급 시도
                loginViewModel.reissueAccessToken { reissueSuccess ->
                    if (reissueSuccess) {
                        // 재발급 성공: 새로운 액세스 토큰으로 진행
                    } else {
                        // 만약 액세스 토큰 재발급이 실패하면, refresh token 재발급(renew) 시도
                        loginViewModel.renewRefreshToken { renewSuccess ->
                            if (!renewSuccess) {
                                // 재발급 실패 시, 로그아웃 처리 (로그인 화면으로 전환)
                                navigateToLogin()
                            }
                        }
                    }
                }
            }
            // 두 토큰 모두 만료된 경우
            else -> {
                // 바로 로그인 화면으로 이동
                navigateToLogin()
            }
        }
    }

    //로그인 액티비티 이동 함수
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

}