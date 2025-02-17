package com.ssafy.witch.ui.group

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupCreateBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch

private const val TAG = "GroupCreateFragment"
class GroupCreateFragment : BaseFragment<FragmentGroupCreateBinding>(FragmentGroupCreateBinding::bind, R.layout.fragment_group_create) {

    private val viewModel: EditViewModel by viewModels()
    private lateinit var imagePickerUtil: ImagePicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupCreateFgBtnPhotoChange.setOnClickListener {
            lifecycleScope.launch {
                // 이미지 업로드
                viewModel.setGroupName(binding.groupCreateFgEtGroupName.text.toString())
                viewModel.uploadImage("create", requireContext() as ContentActivity)
            }
        }
        imagePickerUtil = ImagePicker(this) { uri ->
            viewModel.setFile(uri)
            Glide.with(requireContext())
                .load(uri)
                .into(binding.groupCreateFgIvProfileImage)
        }

        binding.groupCreateFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }
        binding.groupCreateFgBtnPhotoChange.isEnabled = false

        binding.groupCreateFgEtGroupName.doOnTextChanged { text, start, before, count ->
            binding.groupCreateFgBtnPhotoChange.isEnabled = false
        }

        binding.groupCreateFgBtnDuplCheck.setOnClickListener {
            viewModel.checkDupl(binding.groupCreateFgEtGroupName.text.toString(), "group", requireContext() as ContentActivity).apply {
                binding.groupCreateFgBtnPhotoChange.isEnabled = true
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}