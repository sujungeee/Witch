package com.ssafy.witch.ui.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "GroupCreateViewModel"
class EditViewModel : ViewModel() {
    private val _file = MutableLiveData<File?>()
    val file: LiveData<File?>
        get() = _file

    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String>
        get() = _groupName


    fun setFile(file: File) {
        _file.value = file
    }

    fun deleteFile() {
        _file.value = null
    }

    fun setGroupName(groupName: String) {
        _groupName.value = groupName
    }


    fun getPresignedUrl(screen: String) {
        var presignedUrl = PresignedUrl("","")

        viewModelScope.launch {
            runCatching {
                userService.getPresignedUrl("filename.png")
            }.onSuccess {
                if (it.success) {
                    presignedUrl= it.data!!
                    Log.d(TAG, "getPresignedUrl: ${presignedUrl.presignedUrl}")
                    Log.d(TAG, "getPresignedUrl: ${presignedUrl.objectKey}")

                    uploadImgS3(presignedUrl.presignedUrl, presignedUrl.objectKey, screen)
                } else {
                    // 서버에서 success가 false인 경우
                    Log.d(TAG, "getPresignedUrl: ${it.error.errorMessage}")
                }
            }.onFailure {
                // 네트워크 오류 등으로 실패한 경우
                it.printStackTrace()

            }
        }

    }

    fun uploadImgS3(presignedUrl: String, objectKey: String, screen: String) {
        val url = URL(presignedUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "image/png")


        viewModelScope.launch {
            runCatching {
                file.value?.inputStream().use { input ->
                    connection.outputStream.use { output ->
                        input?.copyTo(output) ?: throw Exception("null file")
                    }
                }
            }.onSuccess {
                if (connection.responseCode in 200..299) {
                    if (screen == "edit") {
                        editGroupImage(objectKey)
                    }
                    else if (screen == "create"){
                        createGroup(groupName.value!!,objectKey)
                    } else if( screen == "profile"){
                        editProfileImage(objectKey)
                    }


                    Log.d(TAG, "uploadImgS3: 성공><")
                } else {
                    Log.d(TAG, "uploadImgS3: ${connection.responseCode}")
                }
            }.onFailure {
                throw it
            }
        }

    }

    fun createGroup(groupName: String, groupImage: String) {
        viewModelScope.launch {
            runCatching {
                groupService.createGroup(groupName, groupImage)
            }.onSuccess {
                if (it.success) {
                    Log.d(TAG, "createGroup: 성공><")
                } else {
                    Log.d(TAG, "createGroup: ${it.error.errorMessage}")
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


}