package com.ssafy.witch.ui.appointment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.SnackItem
import com.ssafy.witch.databinding.ActivityMapBinding
import com.ssafy.witch.databinding.BottomSheetLayoutBinding
import com.ssafy.witch.ui.snack.SnackCreateFragment

class MapActivity : BaseActivity<ActivityMapBinding>(ActivityMapBinding::inflate),
    OnMapReadyCallback {
        private val viewModel: MapViewModel by viewModels()
        private lateinit var participantsAdapter: AppointmentDetailParticipantsAdapter
        private lateinit var appointmentSnackAdapter: AppointmentSnackAdatper
        private lateinit var snackList: List<SnackItem>
        private lateinit var participantList: List<AppointmentDetailItem.Participants>

        private lateinit var map: GoogleMap
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private val LOCATION_PERMISSION_REQUEST_CODE = 1

        private var _bottomSheetBinding: BottomSheetLayoutBinding?= null
        private val bottomSheetBinding get() = _bottomSheetBinding!!
        private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map_ac_fg) as SupportMapFragment
            mapFragment.getMapAsync(this)

            _bottomSheetBinding = BottomSheetLayoutBinding.bind(findViewById(R.id.bottom_sheet_layout))

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
                AppointmentDetailItem.Participants(1, "홍길동", "dd1", true),
                AppointmentDetailItem.Participants(2, "권길동", "dd2", false),
                AppointmentDetailItem.Participants(3, "김길동", "dd3", false),
                AppointmentDetailItem.Participants(4, "남길동", "dd4", false),
                AppointmentDetailItem.Participants(5, "임길동", "dd5", false),
                AppointmentDetailItem.Participants(6, "채길동", "dd6", false),
                AppointmentDetailItem.Participants(7, "태길동", "dd7", false),
            )
            initAdapter()

            var appointmentStatus= 1
            showBottomSheetLayout(appointmentStatus)

            var userStatus= 1
            setUserStatus(userStatus, appointmentStatus)

            binding.mapAcTvAppointmentName.isSelected = true

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

        private fun createSnack(userStatus: Int, appointmentStatus: Int) {
            if (appointmentStatus == 2 && (userStatus == 1 || userStatus == 2)) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.map_ac_fg, SnackCreateFragment())
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
            val dialogBuilder = Dialog(this)
            dialogBuilder.setContentView(dialogView)
            dialogBuilder.create()
            dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBuilder.show()

            val appointmentChangeDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
            val appointmentChangeDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

            appointmentChangeDlBtnYes.setOnClickListener {
                when(userStatus) { // TODO

                }
                dialogBuilder.dismiss()
            }

            appointmentChangeDlBtnNo.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }

        private fun setUserStatus(userStatus: Int, appointmentStatus: Int) {
            val drawable = ContextCompat.getDrawable(this, R.drawable.circle_btn) as GradientDrawable

            if (appointmentStatus != 2) {
                binding.mapAcTvAppointmentStatus.visibility = View.GONE
            } else {
                when (userStatus) {
                    1 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 삭제"
                        drawable.setColor(ContextCompat.getColor(this, R.color.witch_red))
                    }
                    2 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 참여"
                        drawable.setColor(ContextCompat.getColor(this, R.color.witch_green))
                    }
                    3 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 탈퇴"
                        drawable.setColor(ContextCompat.getColor(this, R.color.witch_green))
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
            }
            bottomSheetBinding.mapFgRvBottomSnack.adapter= appointmentSnackAdapter

            participantsAdapter= AppointmentDetailParticipantsAdapter(participantList)
            bottomSheetBinding.mapFgRvBottomMembers.adapter= participantsAdapter
        }

        override fun onMapReady(p0: GoogleMap) {
            map = p0
            requestLocationPermissions()
        }

        private fun requestLocationPermissions() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                startLocationUpdates()
            }
        }

        lateinit var destLocation: Location
        private fun startLocationUpdates() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                val location = LatLng(36.108995, 128.421774)
                destLocation= Location("")
                destLocation.latitude= 36.108995
                destLocation.longitude= 128.421774
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

                val userMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.dest)
                val scaledUserMarker = Bitmap.createScaledBitmap(userMarkerBitmap, 130, 130, false)
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("도착지")
                        .icon(BitmapDescriptorFactory.fromBitmap(scaledUserMarker))
                )
            } else {
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates()
                } else {
                    Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            _bottomSheetBinding = null
        }
}