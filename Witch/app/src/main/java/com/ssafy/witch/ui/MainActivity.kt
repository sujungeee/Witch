package com.ssafy.witch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
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

    override fun onResume() {
        super.onResume()
        // 프래그먼트 전환 시 토큰 유효성 체크
        // 리프레쉬 토큰 갱신은 onResume에서 프로액티브하게 처리,  포그라운드 진입 시점에서, refresh token의 만료 또는 갱신 가능 조건을 미리 체크
        Log.d(TAG, "onResume: 리프레쉬 토큰 체크")
        checkTokenValidity()
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
     * 리프레시 토큰 리뉴 가능 시간과 만료시간 사이에 입장시 리프레시 토큰 리뉴
     * 토큰 유효성을 확인하여 리프레시 토큰 만료된 경우 로그인 화면으로 전환.
     */
    private fun checkTokenValidity() {
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        val accessTokenExpiresAt = sharedPref.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = sharedPref.getRefreshTokenExpiresAt()
        val refreshTokenIssuedAt = sharedPref.getRefreshTokenRenewAvailableSeconds()
        val currentTime = System.currentTimeMillis() / 1000

        Log.d(TAG, "현재 시간: $currentTime")
        Log.d(TAG, "AccessToken 만료 시간: $accessTokenExpiresAt")
        Log.d(TAG, "RefreshToken 만료 시간: $refreshTokenExpiresAt")
        Log.d(TAG, "RefreshToken 갱신 가능 시간: $refreshTokenIssuedAt")

        //access Token 만료 및 재발급 여부 확인 후 로그아웃 처리
//        if(currentTime >= accessTokenExpiresAt) {
//            Log.d(TAG, "Access Token 만료됨. 로그인 필요.")
//            navigateToLogin()
//            return
//        }

        // Refresh Token 만료 확인 (7일 기준)
        if (currentTime >= refreshTokenExpiresAt) {
            Log.d(TAG, "Refresh Token 만료됨. 로그인 필요.")
            navigateToLogin()
            return
        }

        // Refresh Token 갱신 가능 여부 확인 (5일 이후)
        val canRenew = (refreshTokenIssuedAt <= currentTime) && (currentTime < refreshTokenExpiresAt)  // 5일 후부터 7일 사이 까지 가능
        if (canRenew) {
            loginViewModel.renewRefreshToken { success ->
                if (!success) {
                    Log.d(TAG, "Refresh Token 갱신 실패. 로그인 필요.")
                    navigateToLogin()
                }
                Log.d(TAG, "Refresh Token 재갱신 성공.")
                Log.d(TAG, "현재 시간: $currentTime")
                Log.d(TAG, "AccessToken 만료 시간 재갱신: $accessTokenExpiresAt")
                Log.d(TAG, "RefreshToken 만료 시간 재갱신: $refreshTokenExpiresAt")
                Log.d(TAG, "RefreshToken 갱신 가능 시간 재갱신 : $refreshTokenIssuedAt")
            }
        } else {
            Log.d(TAG, "Refresh Token 갱신 조건 미충족 (5일 미만)")
        }
    }

    //로그인 액티비티 이동 함수
    private fun navigateToLogin() {
        //자동 로그아웃 시 토큰 다 날리기
        val sharedPref = SharedPreferencesUtil(application.applicationContext)
        sharedPref.clearToken()

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

}