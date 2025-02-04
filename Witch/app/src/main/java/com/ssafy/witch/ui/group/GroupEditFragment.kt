package com.ssafy.witch.ui.group

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupCreateBinding
import com.ssafy.witch.databinding.FragmentGroupEditBinding

class GroupEditFragment : BaseFragment<FragmentGroupEditBinding>(FragmentGroupEditBinding::bind, R.layout.fragment_group_edit) {

    private val viewModel: EditViewModel by viewModels()
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.profileEditFgBtnPhotoChange.setOnClickListener {
            viewModel.getPresignedUrl("edit")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}