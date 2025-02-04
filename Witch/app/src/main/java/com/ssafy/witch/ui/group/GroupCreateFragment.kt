package com.ssafy.witch.ui.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupCreateBinding

class GroupCreateFragment : BaseFragment<FragmentGroupCreateBinding>(FragmentGroupCreateBinding::bind, R.layout.fragment_group_create) {

    private val viewModel: EditViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupCreateFgBtnImageUpload.setOnClickListener {
            // 이미지 업로드
            viewModel.getPresignedUrl("create")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}