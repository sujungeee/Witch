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
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.response.SnackResponse
import com.ssafy.witch.databinding.BottomSheetLayoutBinding
import com.ssafy.witch.databinding.FragmentMapBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.snack.SnackCreateViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "MapFragment_Witch"
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::bind, R.layout.fragment_map),
    OnMapReadyCallback {
        private var userStatus = -1
        private var appointmentId = ""

        private val appointmentViewModel: AppointmentViewModel by activityViewModels()
        private val mapViewModel: MapViewModel by viewModels()
        private val snackViewModel: SnackCreateViewModel by viewModels()

        private lateinit var participantsAdapter: AppointmentDetailParticipantsAdapter
        private val participantsList = mutableListOf<AppointmentDetailItem.Participants>()
        private lateinit var snackList: List<SnackResponse.SnackInfo>

        private var timerFlag = false
        private lateinit var timer: TimerHandler
        private lateinit var map: GoogleMap
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private val userMarkers = mutableMapOf<String, Marker>()

        private var _bottomSheetBinding: BottomSheetLayoutBinding?= null
        private val bottomSheetBinding get() = _bottomSheetBinding!!

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            arguments?.let {
                appointmentId = it.getString("appointmentId").toString()
            }
            appointmentViewModel.getAppointmentInfo(appointmentId)
            appointmentViewModel.getMyInfo()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_ac_fg) as SupportMapFragment
            mapFragment.getMapAsync(this)

            val bottomSheetView = view.findViewById<View>(R.id.bottom_sheet_layout)
            _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)

            initLocationObserver()
            initUserObserver()
            initSnackObserver()
            initAppointmentObserver()

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
                (requireActivity() as ContentActivity).openFragment(5)
            }

            bottomSheetBinding.mapFgIvBottomAdd.setOnClickListener {
                (requireActivity() as ContentActivity).openFragment(5)
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

        private fun createMarkerBitmap(context: Context, imageUrl: String): Bitmap {
            val markerView = LayoutInflater.from(context).inflate(R.layout.appointment_member_item, null)

            val profileImage = markerView.findViewById<ImageView>(R.id.appointment_member_profile_image)

            Glide.with(context)
                .load(imageUrl)
                .circleCrop()
                .into(profileImage)

            markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
            val bitmap = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            markerView.draw(canvas)

            return bitmap
        }

        private fun initUserObserver() {
            mapViewModel.userStatus.observe(viewLifecycleOwner) {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_btn) as GradientDrawable

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

        private fun initLocationObserver() {
            MyLocationForegroundService.locationData.observe(viewLifecycleOwner) {
                appointmentViewModel.getAppointmentInfo(appointmentId)
                appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                    if(timerFlag == false) {
                        setTimer(LocalDateTime.parse(it.appointmentTime))
                        timerFlag = true
                    }
                    showSnackArea(it.appointmentStatus)
                }
                // 위치 표시
                appointmentViewModel.getLocationList(appointmentId)
                if (appointmentViewModel.locationLists.value != null) {
                    for (locationInfo in appointmentViewModel.locationLists.value!!) {
                        val userId = locationInfo.userId
                        val latlng = LatLng(locationInfo.latitude, locationInfo.longitude)
                        val profileImageUrl = locationInfo.profileImageUrl
                        val markerBitmap = createMarkerBitmap(requireContext(), profileImageUrl)

                        if (userMarkers.containsKey(userId)) {
                            userMarkers[userId]?.position = latlng
                        } else {
                            val marker = map.addMarker(
                                MarkerOptions()
                                    .position(latlng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
                            )
                            if (marker != null) {
                                userMarkers[userId] = marker
                            }
                        }
                    }
                }
            }
        }

        private fun initAppointmentObserver() {

            appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                setTimer(LocalDateTime.parse(it.appointmentTime))
                startLocationUpdates(it.latitude.toDouble(), it.longitude.toDouble())

                binding.mapAcTvAppointmentName.text = it.name
                bottomSheetBinding.mapFgTvBottomAppointmentSummary.text = it.summary
                bottomSheetBinding.mapFgTvBottomAppointmentLocation.text = it.address
                bottomSheetBinding.mapFgTvBottomAppointmentTime.text = LocalDateTime.parse(it.appointmentTime).format(
                    DateTimeFormatter.ofPattern("M월 d일 a h시 mm분", Locale.KOREAN))

                var flag = false
                for(participant in it.participants) {
                    if(participant.isLeader) { // 약속장인 경우

                        Glide.with(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile.context)
                            .load(participant.profileImageUrl)
                            .circleCrop()
                            .into(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile)
                        bottomSheetBinding.mapFgTvBottomAppointmentLeader.text = participant.nickname
//                            if(participant.is_late) { // 약속장이 지각한 경우
//                                bottomSheetBinding.mapFgTvBottomLeaderLate.visibility = View.VISIBLE
//                            }

                        if (participant.userId == appointmentViewModel.userId.value) {
                            flag = true
                            mapViewModel.setUserStatus(1)
                        }
                    } else { // 모임원인 경우
                        participantsList.add(participant)

                        if (participant.userId == appointmentViewModel.userId.value) {
                            flag = true
                            mapViewModel.setUserStatus(3)
                        }
                    }
                }
                if (flag == false) {
                    mapViewModel.setUserStatus(2)
                }
                showSnackArea(it.appointmentStatus)
                participantsAdapter= AppointmentDetailParticipantsAdapter(participantsList)
                bottomSheetBinding.mapFgRvBottomMembers.adapter= participantsAdapter
            }

            appointmentViewModel.fragmentIdx.observe(viewLifecycleOwner) {
                val mainActivity = Intent(requireContext(), MainActivity::class.java)
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                mainActivity.putExtra("openFragment", it)
                mainActivity.putExtra("id", appointmentViewModel.groupId.toString())
                startActivity(mainActivity)
            }

            appointmentViewModel.toastMsg.observe(viewLifecycleOwner) { msg ->
                showCustomToast(msg)
            }

        }

        private fun initSnackObserver() {
            snackViewModel.getSnackList(appointmentId)
            snackViewModel.snackList.observe(viewLifecycleOwner) {
                bottomSheetBinding.mapFgRvBottomSnack.adapter= AppointmentSnackAdatper(snackList) {
                    (requireActivity() as ContentActivity).openFragment(4, it)
                }
            }
        }

        private fun setDialog(userStatus: Int) {
            val dialogView= when (mapViewModel.userStatus.value) {
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
                    1 -> appointmentViewModel.deleteAppointment(appointmentId)
                    2 -> appointmentViewModel.participateAppointment(appointmentId)
                    3 -> appointmentViewModel.leaveAppointment(appointmentId)
                }
                dialogBuilder.dismiss()
            }

            appointmentChangeDlBtnNo.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }

        private fun showSnackArea(appointmentStatus: String) {
            if (appointmentStatus == "ONGOING") { // 스낵 보여주기
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.VISIBLE
                binding.mapFgClSnackArea.visibility = View.VISIBLE
            } else if (appointmentStatus == "SCHEDULED") { // 약속 전
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
                binding.mapFgClSnackArea.visibility = View.GONE
            } else if (appointmentStatus == "FINISHED") { // 약속 끝난 이후
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
                binding.mapFgClSnackArea.visibility = View.GONE
            }
        }

        fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context, drawableId)
                ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }


    lateinit var mylocation: Location
        private fun startLocationUpdates(latitude: Double?, longitude: Double?) {
            Log.d(TAG, "startLocationUpdates: latitude: ${latitude}, longitude: ${longitude}")
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (latitude == null || longitude == null) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val userLocation = LatLng(location.latitude, location.longitude)
                            mylocation = Location("")
                            mylocation.latitude = location.latitude
                            mylocation.longitude = location.longitude
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                        } else {
                            Log.d(TAG, "startLocationUpdates: ")
                        }
                    }
                } else {
                    appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner){
                        val placeLocation = LatLng(latitude, longitude)
                        val bitmap = getBitmapFromVectorDrawable(requireContext(), R.drawable.balloon)
                        val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 15f))
                        map.addMarker(
                            MarkerOptions()
                                .position(placeLocation)
                                .icon(markerIcon)
                        )
                    }
                }

            }
        }

        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    appointmentViewModel.getAppointmentInfo(appointmentId)
                    appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                        startLocationUpdates(it.latitude.toDouble(), it.longitude.toDouble())
                    }
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
                appointmentViewModel.getAppointmentInfo(appointmentId)
                appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                    startLocationUpdates(it.latitude.toDouble(), it.longitude.toDouble())
                }
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            requestLocationPermissions()
        }

        @SuppressLint("SetTextI18n")
        private fun setTimer(appointmentTime: LocalDateTime) {
            val duration = Duration.between(LocalDateTime.now(), appointmentTime).seconds
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
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).toHours().toString() + "시간"
                binding.mapAcSbRemainderTime.progress = 3600
            } else { // 약속 시간 하루 초과
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).abs().toDays().toString() + "일"
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