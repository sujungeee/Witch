package com.ssafy.witch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityContentBinding
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.databinding.FragmentGroupApprovalBinding
import com.ssafy.witch.ui.auth.LoginFragmentViewModel
import com.ssafy.witch.ui.group.GroupApprovalViewModel
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.home.HomeViewModel
import com.ssafy.witch.ui.mypage.MyPageFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment

private const val TAG = "GroupJoinActivity"
class GroupJoinActivity : BaseActivity<FragmentGroupApprovalBinding>(FragmentGroupApprovalBinding::inflate) {

    // 메인액티비티 자동로그인 후 토큰만료시간에 따른 토큰 재발급 및 자동 로그아웃 로직 위한 import
    private lateinit var loginViewModel: LoginFragmentViewModel

    private val viewModel: GroupApprovalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.d(TAG, "onCreate: $data")

        loginViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        // 앱 시작 시 또는 액티비티 진입 시 토큰 유효성 체크

        viewModel.getGroupPreview(data?.getQueryParameter("groupId") ?: "")


        binding.groupApprovalFgBtnRequest.setOnClickListener {
            checkTokenValidity()

            val groupId = data?.getQueryParameter("groupId")
            if (groupId != null) {
                viewModel.requestJoinGroup(groupId)
            }
        }

        initObserver()


        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }

    fun initObserver() {
        viewModel.group.observe(this) {
            binding.groupApprovalFgTvGroupExName.text = it.name
            Glide.with(binding.root)
                .load(it.groupImageUrl)
                .into(binding.groupApprovalFgIvGroupImage)
                .onLoadFailed(resources.getDrawable(R.drawable.pot_icon))
        }
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
        intent.putExtra("approval", 1)
        startActivity(intent)
        finish()
    }

}