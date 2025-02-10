package com.ssafy.witch.ui.group

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentGroupCreateBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File

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
            binding.groupCreateFgIvProfileImage.setImageURI(uri)
        }

        binding.groupCreateFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}