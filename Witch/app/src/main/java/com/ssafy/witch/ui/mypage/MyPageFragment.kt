package com.ssafy.witch.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupEditBinding
import com.ssafy.witch.databinding.FragmentMyPageBinding
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.MainActivity

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::bind, R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mypageFgBtnProfileEdit.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, ProfileEditFragment()).addToBackStack("").commit()
        }

        binding.mypageFgLlPrivateSetting.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, SettingFragment()).addToBackStack("").commit()
        }

        //로그아웃 기능 -> 커스텀 다이얼로그 연결
        binding.mypageFgTvLogout.setOnClickListener {
            showLogoutDialog()
        }
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // "네" 버튼: 로그아웃 처리
        dialogView.findViewById<Button>(R.id.dl_btn_yes).setOnClickListener {
            // JWT 토큰 및 사용자 정보 삭제
            ApplicationClass.sharedPreferencesUtil.clearToken()
            // 회원탈퇴와 달리 로컬 저장 데이터만 삭제
            ApplicationClass.sharedPreferencesUtil.deleteJoinUser()

            // 로그인 액티비티로 전환 (기존 스택 모두 클리어)
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            alertDialog.dismiss()
        }

        // "아니오" 버튼: 다이얼로그 닫기
        dialogView.findViewById<Button>(R.id.dl_btn_no).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}