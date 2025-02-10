package com.ssafy.witch.ui.group

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.ssafy.witch.R
import com.ssafy.witch.databinding.DialogSnackRecordBinding
import com.ssafy.witch.ui.snack.SnackCreateViewModel


private const val TAG = "SnackRecord"
class SnackRecord(    private val fragment: Fragment,
                      private val viewModel: SnackCreateViewModel,
                      private val requestPermissionLauncher: ActivityResultLauncher<String>
) {
    private var audioName=""

    private  var mediaRecorder: MediaRecorder = MediaRecorder()

    private lateinit var mediaPlayer: MediaPlayer

    fun recordDialog() {
        val dialogBinding= DialogSnackRecordBinding.inflate(fragment.layoutInflater)
        dialogBinding.snackRecordDlTvFilePlay.isGone = true
        val dialog = Dialog(fragment.requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setOnDismissListener {
            stopRecord(true)
        }

        dialogBinding.snackRecordDlTvTitle.text = "스낵을 입력해주세요"

        dialogBinding.snackRecordDlBtnYes.setOnClickListener {
            viewModel.setAudio(Uri.parse(audioName))
            dialog.dismiss()
        }

        dialogBinding.snackRecordDlBtnNo.setOnClickListener {
            viewModel.deleteAudio()
            dialog.dismiss()
        }


        dialogBinding.snackRecordDlIvRecord.setOnClickListener {
            initRecord(dialogBinding)
            if (audioName.isNotEmpty() && viewModel.recordState.value == false) {
                dialogBinding.snackRecordDlTvFilePlay.isGone = false
                dialogBinding.snackRecordDlTvFileText.setOnClickListener {
                    playRecord()
                }
            }else{
                dialogBinding.snackRecordDlTvFilePlay.isGone = true
            }
        }
        dialog.show()
    }

    fun initRecord(dialogBinding: DialogSnackRecordBinding) {
        if (viewModel.recordState.value == false) {
            viewModel.setRecordState(true)
            dialogBinding.snackRecordDlTvFileText.text = "녹음중"
            dialogBinding.snackRecordDlIvRecord.setImageResource(R.drawable.record_stop)

            if (ActivityCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startRecord()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            }


        } else {
            viewModel.setRecordState(false)
            dialogBinding.snackRecordDlTvFileText.text = "녹음 시작"
            dialogBinding.snackRecordDlIvRecord.setImageResource(R.drawable.record_start)
            stopRecord()
        }

    }

    fun startRecord() {
        val path = fragment.requireContext().getExternalFilesDir("/")?.absolutePath

        audioName = path + "/record.mp3"


        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(audioName)

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun stopRecord(onDismiiss: Boolean = false) {

        if(viewModel.recordState.value == true ){
            mediaRecorder.stop()
        }

        mediaRecorder.release()

        if(onDismiiss) {
            audioName = ""
        }
    }


    fun playRecord() {
        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer.setDataSource(viewModel.audioFile.value.toString())
            mediaPlayer.prepare()
            mediaPlayer.start()
            Log.d(TAG, "playRecord: ")
        } catch (e: Exception) {
            Log.d(TAG, "playRecord: $e")
            e.printStackTrace()
        }
    }
}