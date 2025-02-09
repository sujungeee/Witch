package com.ssafy.witch.ui.group

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.s3Service
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val TAG = "GroupCreateViewModel"
class EditViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _file = MutableLiveData<Uri?>()
    val file: LiveData<Uri?>
        get() = _file

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName


    fun setFile(file: Uri) {
        _file.value = file
    }

    fun deleteFile() {
        _file.value = null
    }

    fun setGroupName(groupName: String) {
        _groupName.value = groupName
    }

    fun setName(name: String) {
        _name.value = name
    }

    suspend fun uploadImage(screen: String, context: ContentActivity) {
        runCatching {
            val presignedUrl = getPresignedUrl(screen)
            val response = uploadImgS3(presignedUrl.presignedUrl, context )

            if (response) {
                when (screen) {
                    "edit" -> editGroupImage(presignedUrl.objectKey)
                    "create" -> createGroup(groupName.value!!, presignedUrl.objectKey, context)
                    "profile" -> editProfileImage(presignedUrl.objectKey)
                }
            } else {
                throw Exception("업로드 실패")
            }
        }.onFailure {
            it.printStackTrace()
        }
    }



    suspend fun getPresignedUrl(screen: String): PresignedUrl {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                runCatching {
                    when (screen) {
                        "edit" -> groupService.getPresignedUrl("filename.png")
                        "create" -> groupService.getPresignedUrl("filename.png")
                        "profile" ->
                            userService.getPresignedUrl("filename.png")

                        else -> {
                            throw Exception("잘못된 screen")
                        }
                    }
                }.onSuccess {
                    if (it.success) {
                        val presignedUrl = it.data!!
                        Log.d(TAG, "getPresignedUrl: ${presignedUrl.presignedUrl}")
                        Log.d(TAG, "getPresignedUrl: ${presignedUrl.objectKey}")

                        continuation.resume(presignedUrl)
                    } else {
                        Log.d(TAG, "getPresignedUrl: ${it.error.errorMessage}")
                        continuation.resumeWithException(Exception(it.error.errorMessage))
                    }
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(it)
                }
            }
        }
    }


    suspend fun uploadImgS3(presignedUrl: String, context: Context): Boolean {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                runCatching {
                    Log.d(TAG, "uploadImgS3: ${file.value}")
                    val file = getFileFromUri(context, file.value)
                    val requestBody = file.asRequestBody("image/jpeg".toMediaType())

                    s3Service.uploadFile(presignedUrl, requestBody)
                }.onSuccess {
                    if (it.isSuccessful) {
                        Log.d(TAG, "uploadImgS3: 성공><")
                        continuation.resume(true)
                    } else {
                        Log.d(TAG, "uploadImgS3: 실패><")
                        continuation.resumeWithException(Exception("업로드 실패"))
                    }
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(it)
                }
            }
        }
    }

    fun createGroup(groupName: String, groupImageObjectKey: String, context: ContentActivity) {
        viewModelScope.launch {
            runCatching {
                groupService.createGroup(GroupInfo(groupName, groupImageObjectKey))
            }.onSuccess {
                if (it.success) {
                    context.finish()
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editGroupImage(objectKey: String) {
        viewModelScope.launch {
            runCatching {
                groupService.editGroupImage(objectKey)
            }.onSuccess {
                if (it.success) {
                    Log.d(TAG, "editGroupImage: 성공><")
                } else {
                    Log.d(TAG, "editGroupImage: ${it.error.errorMessage}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editGroupName(groupName: String) {
        viewModelScope.launch {
            runCatching {
                groupService.editGroupName(groupName)
            }.onSuccess {
                if (it.success) {
                    Log.d(TAG, "editGroupName: 성공><")
                } else {
                    Log.d(TAG, "editGroupName: ${it.error.errorMessage}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editProfileImage(objectKey: String) {
        viewModelScope.launch {
            runCatching {
                userService.editProfileImage(objectKey)
            }.onSuccess {
                if (it.success) {
                    Log.d(TAG, "editProfileImage: 성공><")
                } else {
                    Log.d(TAG, "editProfileImage: ${it.error.errorMessage}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun editProfileName(name: String) {
        viewModelScope.launch {
            runCatching {
                userService.editProfileName(name)
            }.onSuccess {
                if (it.success) {
                    Log.d(TAG, "editProfileName: 성공><")
                } else {
                    Log.d(TAG, "editProfileName: ${it.error.errorMessage}")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri?): File {
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
        val file = File(context.cacheDir, "upload_image.jpg") // 임시 저장소에 파일 생성
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }



}