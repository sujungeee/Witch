package com.ssafy.witch.ui.appointment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.Snack
import com.ssafy.witch.data.model.dto.Snacks
import com.ssafy.witch.databinding.BottomSheetLayoutBinding
import com.ssafy.witch.databinding.FragmentMapBinding
import com.ssafy.witch.ui.ContentActivity
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TAG = "MapFragment_Witch"
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::bind, R.layout.fragment_map),
    OnMapReadyCallback {
        private var userStatus = 3
        private var appointmentStatus = ""

        private var appointmentId = ""

        private val appointmentViewModel: AppointmentViewModel by activityViewModels()
        private val mapViewModel: MapViewModel by activityViewModels()

        private lateinit var participantsAdapter: AppointmentDetailParticipantsAdapter
        private val participantsList = mutableListOf<AppointmentDetailItem.Participants>()
        private lateinit var appointmentSnackAdapter: AppointmentSnackAdatper
        private lateinit var snackList: List<Snacks.SnackItem>

        private lateinit var timer: TimerHandler
        private lateinit var map: GoogleMap
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private val LOCATION_PERMISSION_REQUEST_CODE = 1

        private var _bottomSheetBinding: BottomSheetLayoutBinding?= null
        private val bottomSheetBinding get() = _bottomSheetBinding!!
        private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            startWorkManager()

            snackList= listOf(
                Snacks.SnackItem("1", "김민지", "2025-02-01", "https://witch-app.s3.ap-northeast-2.amazonaws.com/witch-app/profile/efd1144e-f1b3-4b64-9eb3-b63357369e28.png", "https://witch-s3.s3.ap-northeast-2.amazonaws.com/snack/1.mp3", 37.5665, 126.9780),
                Snacks.SnackItem("2", "김유라", "2025-02-01", "https://witch-app.s3.ap-northeast-2.amazonaws.com/witch-app/profile/efd1144e-f1b3-4b64-9eb3-b63357369e28.png", "https://witch-s3.s3.ap-northeast-2.amazonaws.com/snack/2.mp3", 37.5665, 126.9780),
            )
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            appointmentViewModel.getMyInfo()

            arguments?.let {
                appointmentId = it.getString("appointmentId").toString()
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_ac_fg) as SupportMapFragment
            mapFragment.getMapAsync(this)

            val bottomSheetView = view.findViewById<View>(R.id.bottom_sheet_layout)
            _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)

            initAppointmentObserver()
            initSnackObserver()

            binding.mapAcTvAppointmentName.isSelected = true
            bottomSheetBinding.mapFgTvBottomAppointmentSummary.isSelected= true
            bottomSheetBinding.mapFgTvBottomAppointmentLocation.isSelected= true
            bottomSheetBinding.mapFgTvBottomAppointmentLeader.isSelected= true

            binding.mapAcSbRemainderTime.setOnTouchListener { _, _ ->
                true
            }

            binding.mapAcTvAppointmentStatus.setOnClickListener {
                setDialog(userStatus)
            }

            binding.mapAcIvSnack.setOnClickListener {
                createSnack(userStatus, appointmentStatus)
            }

            bottomSheetBinding.mapFgIvBottomAdd.setOnClickListener {
                createSnack(userStatus, appointmentStatus)
            }

            bottomSheetBinding.mapFgCbAppointmentIsLate.setOnCheckedChangeListener { _, isChecked ->
                val lateParticipants = if (isChecked) {
//                    appointmentViewModel.appointmentInfo.value?.participants?.filter { it.is_late } ?: emptyList()
                    appointmentViewModel.appointmentInfo.value?.participants ?: emptyList()

                } else {
                    appointmentViewModel.appointmentInfo.value?.participants ?: emptyList()
                }

//                participantsAdapter.updateList(lateParticipants)
            }

        }

        private fun startWorkManager() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                "LocationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        private fun initAppointmentObserver() {
            appointmentViewModel.userId.observe(viewLifecycleOwner) {

                appointmentViewModel.getAppointmentInfo(appointmentId)
                appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                    setTimer(LocalDateTime.parse(it.appointmentTime))

                    binding.mapAcTvAppointmentName.text = it.name
                    bottomSheetBinding.mapFgTvBottomAppointmentSummary.text = it.summary
                    bottomSheetBinding.mapFgTvBottomAppointmentLocation.text = it.address
                    bottomSheetBinding.mapFgTvBottomAppointmentLeader.text = it.name
                    bottomSheetBinding.mapFgTvBottomAppointmentTime.text = LocalDateTime.parse(it.appointmentTime).format(
                        DateTimeFormatter.ofPattern("M월 d일 a h시 mm분", Locale.KOREAN))


                    for(participant in it.participants) {
                        if(participant.isLeader) { // 약속장인 경우

                            Glide.with(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile.context)
                                .load(participant.profileImageUrl)
                                .circleCrop()
                                .into(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile)

                            bottomSheetBinding.mapFgTvBottomAppointmentLeader.text = participant.nickname
//                            if(participant.is_late) {
//                                bottomSheetBinding.mapFgTvBottomLeaderLate.visibility = View.VISIBLE
//                            }
                            if (participant.userId == appointmentViewModel.userId.value) {
                                userStatus = 1
                            }
                        } else { // 약속원인 경우
                            participantsList.add(participant)

                            if (participant.userId == appointmentViewModel.userId.value) {
                                userStatus = 2
                            }
                        }
                    }
                    
                    // TODO: appointmentStatus 해결
//                    setUserStatus(userStatus, it.appointmentStatus)
                    setUserStatus(userStatus, appointmentStatus)
                    showBottomSheetLayout(appointmentStatus)

                    participantsAdapter= AppointmentDetailParticipantsAdapter(participantsList)
                    bottomSheetBinding.mapFgRvBottomMembers.adapter= participantsAdapter
                }
            }
        }

        private fun initSnackObserver() {
            appointmentSnackAdapter= AppointmentSnackAdatper(snackList) { position ->
                (requireActivity() as ContentActivity).openFragment(4)
            }
            bottomSheetBinding.mapFgRvBottomSnack.adapter= appointmentSnackAdapter
        }

        private fun createSnack(userStatus: Int, appointmentStatus: String) {
            if (appointmentStatus == "ONGOING" && (userStatus == 1 || userStatus == 2)) {
                (requireActivity() as ContentActivity).openFragment(5)
            } else {
                if (appointmentStatus == "SCHEDULED" || appointmentStatus == "FINISHED") {
                    showCustomToast("약속 진행 상태가 아닙니다.")
                }
                if (userStatus == 3) {
                    showCustomToast("약속 참여 대상이 아닙니다.")
                }
            }
        }

        private fun setDialog(userStatus: Int) {
            val dialogView= when (userStatus) {
                1 -> layoutInflater.inflate(R.layout.dialog_appointment_delete, null)
                2 -> layoutInflater.inflate(R.layout.dialog_appointment_join, null)
                3 -> layoutInflater.inflate(R.layout.dialog_appointment_leave, null)
                else -> layoutInflater.inflate(R.layout.dialog_appointment_join, null)
            }
            val dialogBuilder = Dialog(requireContext())
            dialogBuilder.setContentView(dialogView)
            dialogBuilder.create()
            dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBuilder.show()

            val appointmentChangeDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
            val appointmentChangeDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

            appointmentChangeDlBtnYes.setOnClickListener {
                when(userStatus) {
                    1 -> appointmentViewModel.deleteAppointment(1)
                    2 -> appointmentViewModel.participateAppointment(1)
                    3 -> appointmentViewModel.leaveAppointment(1)
                }
                dialogBuilder.dismiss()
            }

            appointmentChangeDlBtnNo.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }

        private fun setUserStatus(userStatus: Int, appointmentStatus: String) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_btn) as GradientDrawable

            if (appointmentStatus == "ONGOING" || appointmentStatus == "FINISHED") {
                binding.mapAcTvAppointmentStatus.visibility = View.GONE
            } else {
                when (userStatus) {
                    1 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 삭제"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_red))
                    }
                    2 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 참여"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_green))
                    }
                    3 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 탈퇴"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_green))
                    }
                }

                binding.mapAcTvAppointmentStatus.background = drawable
            }
        }

        private fun showBottomSheetLayout(appointmentStatus: String) {
            if (appointmentStatus == "ONGOING") { // 스낵 보여주기
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.VISIBLE
            } else if (appointmentStatus == "SCHEDULED") { // 약속 전
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
            } else if (appointmentStatus == "FINISHED") { // 약속 끝난 이후
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
            }
        }

        lateinit var mylocation: Location
        private fun startLocationUpdates() {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val userLocation = LatLng(location.latitude, location.longitude)
                        mylocation = Location("")
                        mylocation.latitude = location.latitude
                        mylocation.longitude = location.longitude
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    } else {
                    }
                }
            }
        }

        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startLocationUpdates()
                } else {
                    Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        private fun requestLocationPermissions() {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                startLocationUpdates()
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            requestLocationPermissions()
        }

        fun getBitmapFromVectorDrawable(context: Context, drawableId: Int) : Bitmap {
            val drawable = ContextCompat.getDrawable(context, drawableId)
                ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }

        @SuppressLint("SetTextI18n")
        private fun setTimer(appointmentTime: LocalDateTime) {
            val duration = Duration.between(LocalDateTime.now(), appointmentTime).seconds
            mapViewModel.setRemainderTime(duration)

            if (duration < 0) { // 이미 끝난 약속
                binding.mapAcTvRemainderTime.text = "0분 0초"
                binding.mapAcSbRemainderTime.progress = 0
                appointmentStatus = "FINISHED" // TODO: delete
            } else if (duration in 0..3600) { // 약속 시간 한 시간 이내
                timer = TimerHandler()
                timer.startTimer(duration)
                mapViewModel.remainderTime.observe(viewLifecycleOwner) { remainingTime ->
                    binding.mapAcTvRemainderTime.text = remainingTime?.let { parseSeconds(it) }
                    binding.mapAcSbRemainderTime.progress = remainingTime?.toInt() ?: 0
                }
                appointmentStatus = "ONGOING" // TODO: delete
            } else if (duration < 3600 * 24) { // 약속 시간 하루 이내
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).toHours().toString() + "시간"
                binding.mapAcSbRemainderTime.progress = 3600
                appointmentStatus = "SCHEDULED" // TODO: delete
            } else { // 약속 시간 하루 초과
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).abs().toDays().toString() + "일"
                binding.mapAcSbRemainderTime.progress = 3600
                appointmentStatus = "SCHEDULED" // TODO: delete
            }
        }
    
        private fun parseSeconds(seconds: Long): String {
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            return "${minutes}분 ${remainingSeconds}초"
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _bottomSheetBinding = null
        }

        @SuppressLint("HandlerLeak")
        inner class TimerHandler : Handler(Looper.getMainLooper()) {
            private var remainingTime: Long = 0
            private var isRunning = false

            fun startTimer(initialTime: Long) {
                isRunning = true
                remainingTime = initialTime
                removeCallbacksAndMessages(null)
                post(timerRunnable)
            }

            private val timerRunnable = object : Runnable {
                override fun run() {
                    if (!isRunning) return

                    if (remainingTime > 0) {
                        remainingTime -= 1
                        mapViewModel.setRemainderTime(remainingTime)
                    }

                    postDelayed(this, 1000)
                }
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }
}