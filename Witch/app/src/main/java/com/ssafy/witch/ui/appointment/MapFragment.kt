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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.SnackItem
import com.ssafy.witch.databinding.BottomSheetLayoutBinding
import com.ssafy.witch.databinding.FragmentMapBinding
import com.ssafy.witch.ui.ContentActivity
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private const val TAG = "MapFragment_Witch"
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::bind, R.layout.fragment_map),
    OnMapReadyCallback {
        private var userStatus = -1
        private var appointmentStatus = -1

        private val appointmentViewModel: AppointmentViewModel by activityViewModels()
        private val mapViewModel: MapViewModel by activityViewModels()
        private lateinit var participantsAdapter: AppointmentDetailParticipantsAdapter
        private lateinit var appointmentSnackAdapter: AppointmentSnackAdatper
        private lateinit var snackList: List<SnackItem>
        private lateinit var participantList: List<AppointmentDetailItem.Participants>

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
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack),
                SnackItem(1, R.drawable.example_snack)
            )

            participantList= listOf(
                AppointmentDetailItem.Participants(1, "홍길동", "dd1", true, false),
                AppointmentDetailItem.Participants(2, "권길동", "dd2", false, false),
                AppointmentDetailItem.Participants(3, "김길동", "dd3", false, false),
                AppointmentDetailItem.Participants(4, "남길동", "dd4", false, false),
                AppointmentDetailItem.Participants(5, "임길동", "dd5", false, false),
                AppointmentDetailItem.Participants(6, "채길동", "dd6", false, false),
                AppointmentDetailItem.Participants(7, "태길동", "dd7", false, false),
            )
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_ac_fg) as SupportMapFragment
            mapFragment.getMapAsync(this)

            val bottomSheetView = view.findViewById<View>(R.id.bottom_sheet_layout)
            _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)

            initView()
            initAdapter()

            appointmentStatus= 1
            userStatus= 1 // 1: 약속장 약속 삭제, 2: 약속 모임원 약속 참여 3. 약속 모임원 약속 탈퇴
            showBottomSheetLayout(appointmentStatus)
            setUserStatus(userStatus, appointmentStatus)

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

            binding.mapAcIvSnack.setOnClickListener { // 진저 브레드
                createSnack(userStatus, appointmentStatus)
            }

            bottomSheetBinding.mapFgIvBottomAdd.setOnClickListener { // 스낵 영역 내에 있는 스낵 버튼
                createSnack(userStatus, appointmentStatus)
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

        private fun initView() {
            setTimer()
        }

        private fun createSnack(userStatus: Int, appointmentStatus: Int) {
            if (appointmentStatus == 2 && (userStatus == 1 || userStatus == 2)) {
                val contentActivity = Intent(requireContext(), ContentActivity::class.java)
                contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                contentActivity.putExtra("openFragment", 5)
                startActivity(contentActivity)
            } else {
                if (appointmentStatus == 1 || appointmentStatus == 3) {
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
                when(userStatus) { // TODO
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

        private fun setUserStatus(userStatus: Int, appointmentStatus: Int) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_btn) as GradientDrawable

            if (appointmentStatus != 2) {
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

        private fun showBottomSheetLayout(appointmentStatus: Int) {
            if (appointmentStatus == 1) { // 스낵 보여주기
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.VISIBLE
            } else if (appointmentStatus == 2) { // 약속 전
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
            } else if (appointmentStatus == 3) { // 약속 끝난 이후
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.VISIBLE
            }
        }

        fun initAdapter(){
            appointmentSnackAdapter= AppointmentSnackAdatper(snackList) { position ->
                (requireActivity() as ContentActivity).openFragment(4)
            }
            bottomSheetBinding.mapFgRvBottomSnack.adapter= appointmentSnackAdapter

            participantsAdapter= AppointmentDetailParticipantsAdapter(participantList)
            bottomSheetBinding.mapFgRvBottomMembers.adapter= participantsAdapter
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

        override fun onResume() {
            super.onResume()
            setTimer()
        }

        @SuppressLint("SetTextI18n")
        private fun setTimer() {
            val dateTimeString = "2025-02-06T16:45"
            val targetDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val duration = Duration.between(LocalDateTime.now(), targetDateTime).seconds
            mapViewModel.setRemainderTime(duration)
            if (duration < 0) { // 이미 끝난 약속
                binding.mapAcTvRemainderTime.text = "0분 0초"
                binding.mapAcSbRemainderTime.progress = 0
            } else if (duration in 0..3600) { // 약속 시간 한 시간 이내
                timer = TimerHandler()
                timer.startTimer(duration)
                mapViewModel.remainderTime.observe(viewLifecycleOwner) { remainingTime ->
                    binding.mapAcTvRemainderTime.text = remainingTime?.let { parseSeconds(it) }
                    binding.mapAcSbRemainderTime.progress = remainingTime?.toInt() ?: 0
                }
            } else if (duration < 3600 * 24) { // 약속 시간 하루 이내
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), targetDateTime).toHours().toString() + "시간"
                binding.mapAcSbRemainderTime.progress = 3600
            } else { // 약속 시간 하루 초과
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), targetDateTime).abs().toDays().toString() + "일"
                binding.mapAcSbRemainderTime.progress = 3600
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
}