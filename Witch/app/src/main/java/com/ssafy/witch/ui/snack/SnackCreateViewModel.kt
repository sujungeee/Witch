package com.ssafy.witch.ui.snack

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.model.dto.Snack
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.model.response.SnackResponse
import com.ssafy.witch.data.remote.RetrofitUtil
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.appointmentService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.groupService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.s3Service
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.snackService
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.userService
import com.ssafy.witch.data.remote.S3Service
import com.ssafy.witch.ui.ContentActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

private const val TAG = "SnackCreateViewModel_Witch"
class SnackCreateViewModel : ViewModel() {
    val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage



    private val _selectedButton = MutableLiveData<Int>(1)
    val selectedButton: LiveData<Int>
        get() = _selectedButton

    private val _snackText = MutableLiveData<String>("")
    val snackText: LiveData<String>
        get() = _snackText


    private val _photoFile = MutableLiveData<Uri?>(null)
    val photoFile: LiveData<Uri?>
        get() = _photoFile


    private val _audioFile = MutableLiveData<Uri?>(null)
    val audioFile: LiveData<Uri?>
        get() = _audioFile


    private val _recordState = MutableLiveData<Boolean>(false)
    val recordState: LiveData<Boolean>
        get() = _recordState


    private val _textColor = MutableLiveData<Int>(R.id.text_color_black)
    val textColor: LiveData<Int>
        get() = _textColor


    private val _textAlign = MutableLiveData<Int>(R.id.top_start)
    val textAlign: LiveData<Int>
        get() = _textAlign

    private val _snackList = MutableLiveData<List<SnackResponse.SnackInfo>>()
    val snackList: LiveData<List<SnackResponse.SnackInfo>>
        get() = _snackList

    fun setSnackText(text: String) {
        _snackText.value = text
    }

    fun setPhoto(file: Uri?) {
        _photoFile.value = file
    }

    fun setAudio(file: Uri?) {
        _audioFile.value = file
    }

    fun setSelectedButton(button: Int) {
        _selectedButton.value = button
    }

    fun deleteText() {
        _snackText.value = ""
    }

    fun deletePhoto() {
        _photoFile.value = null
    }

    fun deleteAudio() {
        _audioFile.value = null
    }

    fun setRecordState(state: Boolean) {
        _recordState.value = state
    }

    fun setTextColor(color: Int) {
        _textColor.value = color
    }

    fun setTextAlign(align: Int) {
        _textAlign.value = align
    }

    suspend fun uploadSnack(context: ContentActivity,appointmentId: String, location: LatLng, image: Uri, audio: Uri?) {
        var audioPresignedUrl = PresignedUrl("", "")
        runCatching {
            val imagePresignedUrl = getPresignedUrl("image")
            if (_audioFile.value != null && _audioFile.value != Uri.EMPTY) {
                audioPresignedUrl = getPresignedUrl("audio")
            }

            val response= uploadSnackToS3(imagePresignedUrl, audioPresignedUrl, image, audio, context)

            if (response) {
                createSnack(appointmentId, Snack(location.latitude, location.longitude, imagePresignedUrl.objectKey, audioPresignedUrl.objectKey), context)
                Log.d("uploadSnack", "업로드 성공")
            } else {
                throw Exception("업로드 실패")
            }

        }

    }

    suspend fun getPresignedUrl(type: String): PresignedUrl {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                runCatching {
                    when (type) {
                        "image" -> snackService.getImagePresignedUrl("filename.png")
                        "audio" -> snackService.getAudioPresignedUrl("filename.mp3")
                        else -> {
                            throw Exception("잘못된 type")
                        }
                    }
                }.onSuccess {
                    if (it.isSuccessful) {
                        val presignedUrl = it.body()?.data!!
                        continuation.resume(presignedUrl)
                    } else {
                        continuation.resumeWithException(Exception(it.errorBody().toString()))
                    }
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(it)
                }
            }
        }
    }


    suspend fun uploadSnackToS3(imagePresignedUrl: PresignedUrl, audioPresignedUrl: PresignedUrl, imageUri: Uri, audioUri: Uri?, context: Context) :Boolean {
        var requestBody1 = RequestBody.create("audio/mp3".toMediaType(), File(context.cacheDir, "upload_audio.mp3"))
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                val imageFile = getFileFromUri(context, imageUri)
                val requestBody = imageFile.asRequestBody("image/jpeg".toMediaType())

                if (audioUri != null && audioUri != Uri.EMPTY) {
                    val audioFile = getFileFromUri(context, audioUri)
                    requestBody1 = audioFile.asRequestBody("audio/mp3".toMediaType())
                }

                runCatching {
                    s3Service.uploadFile(imagePresignedUrl.presignedUrl, requestBody)
                    if (audioUri != null && audioUri != Uri.EMPTY) {
                        s3Service.uploadFile(audioPresignedUrl.presignedUrl, requestBody1)
                    }
                }.onSuccess {
                    continuation.resume(true)
                }.onFailure {
                    it.printStackTrace()
                    continuation.resumeWithException(Exception("업로드 실패"))
                }
            }
        }
    }


    fun getFileFromUri(context: Context, uri: Uri?): File {
        Log.d(TAG, "getFileFromUri: $uri")

        return when (uri?.scheme) {
            "file" -> File(uri.path!!) // 🔥 직접 File 변환
            "content" -> {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, "upload_audio.mp3") // 🔥 파일명 변경
                inputStream.use { input ->
                    file.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }
                file
            }
            else -> throw IllegalArgumentException("지원하지 않는 Uri 형식")
        }
    }



    fun createSnack(appointmentId:String, snack: Snack, context: ContentActivity){
        viewModelScope.launch {
            runCatching {
                snackService.createSnack(appointmentId,snack)
            }.onSuccess {
                if (it.isSuccessful) {
                    context.finish()
                    Toast.makeText(context, "스낵이 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("createSnack", "스낵 생성 성공")
                } else {
                    val data = Gson().fromJson(it.errorBody()?.string(), BaseResponse::class.java)
                    _errorMessage.value = data.error.errorMessage
                }

            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getSnackList(appointmentId: String) {
        viewModelScope.launch {
            runCatching {
                snackService.getSnackList(appointmentId)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _snackList.value = response.body()?.data!!.snacks
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = errorBody?.let {
                        val type = object : TypeToken<BaseResponse<ErrorResponse>>() {}.type
                        Gson().fromJson<BaseResponse<ErrorResponse>>(it, type)
                    }
                    Log.d(TAG, "getSnackList failed(): ${errorResponse?.data?.errorMessage}")
                }
            }.onFailure { e ->
                Log.e(TAG, "deleteAppointment() Exception: ${e.message}", e)
            }
        }
    }

}