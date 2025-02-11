package com.ssafy.witch.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentPwdEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity

class PwdEditFragment : BaseFragment<FragmentPwdEditBinding>(FragmentPwdEditBinding::bind, R.layout.fragment_pwd_edit) {

    val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.pwdEditFgBtnChangePwd.setOnClickListener {
            val password = binding.pwdEditFgEtOriginPwd.text.toString()
            val newPassword = binding.pwdEditFgEtNewPwd.text.toString()
            val passwordCheck = binding.pwdEditFgEtNewPwdTwice.text.toString()
            // 비밀번호 변경 로직

            // 비밀번호 일치 여부 체크
            if (newPassword != passwordCheck) {
                Toast.makeText(requireContext(), "새 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 길이가 8~16자리인지 검사
            if (newPassword.length !in 8..16) {
                Toast.makeText(requireContext(), "비밀번호는 8~16자리여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.editPassword(password, passwordCheck, requireActivity() as ContentActivity)

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}