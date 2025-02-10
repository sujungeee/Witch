package com.ssafy.witch.ui.group

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.ssafy.witch.databinding.FragmentGroupEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupEditFragment : BaseFragment<FragmentGroupEditBinding>(FragmentGroupEditBinding::bind, R.layout.fragment_group_edit) {

    private val viewModel: EditViewModel by viewModels()
    private var imageUri: Uri? = null
    private lateinit var imagePickerUtil: ImagePicker



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.groupEditFgBtnPhotoChange.setOnClickListener {
            lifecycleScope.launch {
                viewModel.uploadImage("edit", requireContext() as ContentActivity)
            }
        }

        imagePickerUtil = ImagePicker(this) { uri ->
            viewModel.setFile(uri)
            binding.groupEditFgIvProfileImage.setImageURI(uri)
        }

        binding.groupEditFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}