package com.ssafy.witch.ui.snack

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.DialogSnackTextBinding
import com.ssafy.witch.databinding.FragmentSnackCreateBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.group.SnackRecord
import com.ssafy.witch.util.ImagePicker
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


private const val TAG = "SnackCreateFragment"
class SnackCreateFragment : BaseFragment<FragmentSnackCreateBinding>(FragmentSnackCreateBinding::bind, R.layout.fragment_snack_create) {
    private val viewModel: SnackCreateViewModel by viewModels()

    private lateinit var contentActivity: ContentActivity
    private lateinit var imagePickerUtil: ImagePicker
    private var appointmentId = ""

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            snackRecord?.startRecord()
        } else {
            Toast.makeText(requireContext(), "녹음 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private var snackRecord: SnackRecord? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentActivity = requireActivity() as ContentActivity
        initView()
        initObserver()
    }
    fun initView() {

        binding.snackCreateFgIbText.setOnClickListener {
            viewModel.setSelectedButton(1)
        }

        binding.snackCreateFgIbCamera.setOnClickListener {
            viewModel.setSelectedButton(2)
        }

        binding.snackCreateFgIbRecord.setOnClickListener {
            viewModel.setSelectedButton(3)
        }

        initTextView()
        initPhotoView()
        initRecordView()
        initUploadView()

    }

    private fun initObserver() {
        viewModel.snackText.observe(viewLifecycleOwner, {
            val isSnackTextEmpty = it.isNullOrEmpty()
            binding.snackCreateFgLlTvTextCreate.isGone = !isSnackTextEmpty
            binding.snackCreateFgLlLlText.isGone = isSnackTextEmpty

            binding.snackCreateFgTvBitmapText.text = it
        })

        viewModel.photoFile.observe(viewLifecycleOwner, {
            val isPhotoEmpty = it == null
            binding.snackCreateFgLlLlCamera.isGone = !isPhotoEmpty
            binding.snackCreateFgLlTvCameraDelete.isGone = isPhotoEmpty
        })

        viewModel.audioFile.observe(viewLifecycleOwner, {
            val isAudioEmpty = it == null
            binding.snackCreateFgLlTvRecordCreate.isGone = !isAudioEmpty
            binding.snackCreateFgLlTvRecordDelete.isGone = isAudioEmpty
        })

        viewModel.selectedButton.observe(viewLifecycleOwner, {
            binding.snackCreateFgLlText.isVisible = it == 1
            binding.snackCreateFgLlCamera.isVisible = it == 2
            binding.snackCreateFgLlRecord.isVisible = it == 3
        }
        )

    }



    fun initTextView() {
        binding.snackCreateFgLlTvTextDelete.setOnClickListener {
            viewModel.setTextAlign(R.id.top_start)
            viewModel.setTextColor(R.id.text_color_black)
            viewModel.deleteText()
        }
        binding.snackCreateFgLlTvTextCreate.setOnClickListener {
            textDialog()
        }

        binding.snackCreateFgLlTvTextUpdate.setOnClickListener {
            textDialog(viewModel.snackText.value!!, viewModel.textAlign.value!!, viewModel.textColor.value!!)
        }


    }


    fun textDialog(text: String = "", align: Int = R.id.top_start, color: Int = R.id.text_color_black) {
        val textAlignRg =
            mapOf(
                R.id.top_start to (Gravity.TOP or Gravity.START),
                R.id.top_center to (Gravity.TOP or Gravity.CENTER_HORIZONTAL),
                R.id.top_end to (Gravity.TOP or Gravity.END),
                R.id.center_start to (Gravity.CENTER or Gravity.START),
                R.id.center_center to (Gravity.CENTER),
                R.id.center_end to (Gravity.CENTER or Gravity.END),
                R.id.bottom_start to (Gravity.BOTTOM or Gravity.START),
                R.id.bottom_center to (Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL),
                R.id.bottom_end to (Gravity.BOTTOM or Gravity.END)
            )

        val textColorRg =
            mapOf(
                R.id.text_color_black to Color.BLACK,
                R.id.text_color_white to Color.WHITE,
                R.id.text_color_red to Color.RED,
                R.id.text_color_green to Color.GREEN,
                R.id.text_color_blue to Color.BLUE,
                R.id.text_color_gray to Color.GRAY
            )

        val dialogBinding= DialogSnackTextBinding.inflate(layoutInflater)

        val dialog = Dialog(contentActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.snackTextDlTvTitle.text = "텍스트를 입력해주세요"

        dialogBinding.snackTextDlEtText.setText(text)

        val parmas = FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        parmas.gravity = textAlignRg[align]!!

        binding.snackCreateFgTvBitmapText.layoutParams = parmas

        binding.snackCreateFgTvBitmapText.setTextColor(textColorRg[color]!!)


        dialogBinding.snackTextDlBtnYes.setOnClickListener {
            viewModel.setSnackText(dialogBinding.snackTextDlEtText.text.toString())

            val parmas = FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            parmas.gravity = textAlignRg[viewModel.textAlign.value]!!

            binding.snackCreateFgTvBitmapText.layoutParams = parmas

            binding.snackCreateFgTvBitmapText.setTextColor(textColorRg[viewModel.textColor.value]!!)

            dialog.dismiss()
        }

        dialogBinding.snackTextDlBtnNo.setOnClickListener {
            dialog.dismiss()
        }

        alignText(dialogBinding)

        colorText(dialogBinding)


        dialog.show()
    }

    fun alignText(dialogBinding: DialogSnackTextBinding) {

        var isUserAction = true

        viewModel.setTextAlign(viewModel.textAlign.value!!)

        val textAlignChangeListener =
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if (!isUserAction) return@OnCheckedChangeListener
                viewModel.setTextAlign(checkedId)
            }

        viewModel.textAlign.observe(viewLifecycleOwner) { it ->
            if (!isUserAction) return@observe

            isUserAction = false

            dialogBinding.textAlignRg1.check(viewModel.textAlign.value!!)
            dialogBinding.textAlignRg2.check(viewModel.textAlign.value!!)
            dialogBinding.textAlignRg3.check(viewModel.textAlign.value!!)

            isUserAction = true
        }

        dialogBinding.textAlignRg1.setOnCheckedChangeListener(textAlignChangeListener)
        dialogBinding.textAlignRg2.setOnCheckedChangeListener(textAlignChangeListener)
        dialogBinding.textAlignRg3.setOnCheckedChangeListener(textAlignChangeListener)


    }

    fun colorText(dialogBinding: DialogSnackTextBinding) {

        var isUserAction = true

        viewModel.setTextColor(viewModel.textColor.value!!)

        val textColorChangeListener =
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if (!isUserAction) return@OnCheckedChangeListener
                viewModel.setTextColor(checkedId)
            }

        viewModel.textColor.observe(viewLifecycleOwner) { it ->
            if (!isUserAction) return@observe

            isUserAction = false

            dialogBinding.textColorRg.check(viewModel.textColor.value!!)
            dialogBinding.textColorRg2.check(viewModel.textColor.value!!)


            isUserAction = true
        }

        dialogBinding.textColorRg.setOnCheckedChangeListener(textColorChangeListener)
        dialogBinding.textColorRg2.setOnCheckedChangeListener(textColorChangeListener)

    }

    fun initPhotoView() {
        binding.snackCreateFgLlLlCamera.setOnClickListener {
            binding.snackCreateFgLlLlCamera.isGone = true
            binding.snackCreateFgLlTvCameraDelete.isGone = false
        }

        binding.snackCreateFgLlTvCameraDelete.setOnClickListener {
            viewModel.deletePhoto()
            binding.snackCreateFgIvImage.setImageResource(R.color.witch_black)
        }

        imagePickerUtil = ImagePicker(this) { uri ->
            viewModel.setPhoto(uri)
            binding.snackCreateFgIvImage.setImageURI(uri)
        }

        binding.snackCreateFgLlTvCameraPhoto.setOnClickListener {
            imagePickerUtil.checkPermissionAndOpenGallery()
        }
    }

    fun initRecordView() {
        binding.snackCreateFgLlTvRecordCreate.setOnClickListener {
            val isRecordEmpty = viewModel.audioFile.value == null
            binding.snackCreateFgLlTvRecordCreate.isGone = !isRecordEmpty
            binding.snackCreateFgLlTvRecordDelete.isGone = isRecordEmpty
        }
        binding.snackCreateFgLlTvRecordCreate.setOnClickListener {
            val snackRecord = SnackRecord(this@SnackCreateFragment, viewModel, requestPermissionLauncher)
            snackRecord.recordDialog()
        }

        binding.snackCreateFgLlTvRecordDelete.setOnClickListener {
            viewModel.deleteAudio()
        }

    }

    fun initUploadView() {
        binding.snackCreateFgBtnUpload.setOnClickListener {
            binding.snackCreateFgLlBitmap.post {
                val bitmap = captureView(binding.snackCreateFgLlBitmap)

                val uri = bitmapToUri(bitmap)


                lifecycleScope.launch {
                    viewModel.uploadSnack(contentActivity, uri)
                }
            }
        }
    }

    fun captureView(view: View): Bitmap {

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    fun bitmapToUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            appointmentId = it.getString("appointmentId").toString()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            SnackCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }



}