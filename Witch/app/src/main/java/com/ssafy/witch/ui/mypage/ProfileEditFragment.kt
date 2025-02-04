package com.ssafy.witch.ui.mypage

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentMyPageBinding
import com.ssafy.witch.databinding.FragmentProfileEditBinding
import com.ssafy.witch.ui.group.EditViewModel
import com.ssafy.witch.ui.group.GroupViewModel
import java.io.File


private const val TAG = "ProfileEditFragment"
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::bind, R.layout.fragment_profile_edit) {
    private val viewModel: EditViewModel by viewModels()
    private var imageUri: Uri? = null

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "onRequestPermissionsResult: 권한 허용")
                openGallery()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: 권한 거부")
                Toast.makeText(requireContext(), "권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(){

        initImage()

        binding.profileEditFgBtnNameChange.setOnClickListener {
            viewModel.editProfileName(binding.profileEditFgEtNickname.text.toString())
        }

    }

    fun initImage(){

        binding.profileEditFgBtnImageDelete.setOnClickListener {
            viewModel.deleteFile()
            binding.profileEditFgIvProfileImage.setImageResource(R.drawable.box_style)
        }

        binding.profileEditFgBtnPhotoChange.setOnClickListener {
            viewModel.getPresignedUrl("profile")
        }

        binding.profileEditFgBtnImageUpload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "initView: permission granted")
                openGallery()
            } else {
                Log.d(TAG, "initView: 권한 없음")
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }

    }


    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    imageUri = it
                    it.path?.let { path ->
                        val file = File(path)
                        viewModel.setFile(file)
                        Log.d(TAG, "pickImageLauncher: $file")
                    }
                    binding.profileEditFgIvProfileImage.setImageURI(imageUri)
                }
            }
        }
}
