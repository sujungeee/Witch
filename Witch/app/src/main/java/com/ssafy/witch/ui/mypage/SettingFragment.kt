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


class SettingFragment : BaseFragment<FragmentPwdEditBinding>(FragmentPwdEditBinding::bind, R.layout.fragment_pwd_edit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}