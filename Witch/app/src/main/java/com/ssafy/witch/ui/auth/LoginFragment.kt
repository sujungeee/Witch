package com.ssafy.witch.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentLoginBinding
import com.ssafy.witch.ui.LoginActivity

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
) {

    private lateinit var loginActivity: LoginActivity
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //자동 로그인 시도
        attemptAutoLogin()

        binding.loginFgBtnLogin.setOnClickListener {
            val email = binding.loginFgEtEmail.text.toString().trim()
            val password = binding.loginFgEtPassword.text.toString().trim()

            // 이메일 또는 비밀번호가 비어있는 경우
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "이메일 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 길이가 8~16자리인지 검사
            if (password.length !in 8..16) {
                Toast.makeText(requireContext(), "비밀번호는 8~16자리여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //로그인 요청
            viewModel.login(email, password) { success, message ->
                requireActivity().runOnUiThread {
                    if (success) {
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(requireContext(), message ?: "로그인 실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        //회원가입 터치 시 회원가입 화면으로 넘어가기
        binding.loginFgBtnJoin.setOnClickListener {
            loginActivity.openFragment(2)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //자동 로그인 시도
    private fun attemptAutoLogin() {
        val accessTokenExpiresAt = viewModel.sharedPreferencesUtil.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = viewModel.sharedPreferencesUtil.getRefreshTokenExpiresAt()

        if (accessTokenExpiresAt > getCurrentTime()) {
            navigateToMainActivity() //액세스 토큰이 유효하면 바로 로그인
        } else if (refreshTokenExpiresAt > getCurrentTime()) {
            viewModel.reissueAccessToken { success ->
                if (success) {
                    navigateToMainActivity()
                } else {
                    viewModel.renewRefreshToken { refreshSuccess ->
                        if (refreshSuccess) {
                            navigateToMainActivity()
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis() / 1000
    }

    //로그인 성공 시 메인액티비로 넘어가기
    private fun navigateToMainActivity() {
        loginActivity.openFragment(1)
    }

}