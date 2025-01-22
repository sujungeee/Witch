package com.ssafy.witch.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupEditBinding
import com.ssafy.witch.databinding.FragmentMyPageBinding
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


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}