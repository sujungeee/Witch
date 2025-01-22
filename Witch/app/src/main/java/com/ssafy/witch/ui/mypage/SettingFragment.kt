package com.ssafy.witch.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentProfileEditBinding
import com.ssafy.witch.databinding.FragmentPwdEditBinding
import com.ssafy.witch.databinding.FragmentSettingBinding
import com.ssafy.witch.ui.MainActivity


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::bind, R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingFgLlChangePwd.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, PwdEditFragment()).addToBackStack("").commit()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}