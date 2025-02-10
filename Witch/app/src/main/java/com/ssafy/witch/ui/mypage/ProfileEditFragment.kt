package com.ssafy.witch.ui.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentMyPageBinding
import com.ssafy.witch.databinding.FragmentProfileEditBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.group.EditViewModel
import com.ssafy.witch.ui.group.GroupViewModel
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch
import java.io.File


private const val TAG = "ProfileEditFragment"
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::bind, R.layout.fragment_profile_edit) {
    private val viewModel: EditViewModel by viewModels()
    private lateinit var imagePickerUtil: ImagePicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(){

        binding.profileEditFgEtNickname.setText(ApplicationClass.sharedPreferencesUtil.getUser().nickname)
        Glide.with(binding.root)
            .load(ApplicationClass.sharedPreferencesUtil.getUser().profileImageUrl)
            .into(binding.profileEditFgIvProfileImage)

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
            lifecycleScope.launch {
                viewModel.uploadImage("profile", requireContext() as ContentActivity)
            }
        }

        imagePickerUtil = ImagePicker(this) { uri ->
            viewModel.setFile(uri)
            binding.profileEditFgIvProfileImage.setImageURI(uri)
        }

        binding.profileEditFgBtnImageUpload.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }

    }



    @SuppressLint("Range")
    fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))

        cursor.close()
        return path
    }

    @SuppressLint("Range")
    private fun getPath(uri: Uri): String {
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null )
        cursor?.moveToNext()
        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))

        cursor?.close()

        return path?:""
    }
}
