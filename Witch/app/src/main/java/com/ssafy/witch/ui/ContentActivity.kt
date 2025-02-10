package com.ssafy.witch.ui

import android.os.Bundle
import androidx.fragment.app.replace
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.databinding.ActivityContentBinding
import com.ssafy.witch.ui.appointment.AppointmentCreate1Fragment
import com.ssafy.witch.ui.appointment.AppointmentCreate2Fragment
import com.ssafy.witch.ui.appointment.AppointmentCreate3Fragment
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.mypage.ProfileEditFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment
import com.ssafy.witch.ui.snack.SnackFragment
import com.ssafy.witch.ui.appointment.MapFragment
import com.ssafy.witch.ui.mypage.PwdEditFragment

class ContentActivity  : BaseActivity<ActivityContentBinding>(ActivityContentBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentIdx = intent.getIntExtra("openFragment", -1)
        val id = intent.getStringExtra("id") ?: ""
        if (fragmentIdx != -1) {
            openFragment(fragmentIdx, id)
            intent.removeExtra("openFragment")
        }
    }

    fun openFragment(index: Int, id : String = ""){
        moveFragment(index , id)
    }

    private fun moveFragment(index:Int, id : String = ""){
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> transaction.replace(R.id.content_flayout, GroupCreateFragment())
            2 -> transaction.replace(R.id.content_flayout, GroupEditFragment.newInstance("groupId", id))
            3 -> transaction.replace(R.id.content_flayout, ProfileEditFragment())
            4 -> transaction.replace(R.id.content_flayout, SnackFragment.newInstance("snackId", id))
            5 -> transaction.replace(R.id.content_flayout, SnackCreateFragment.newInstance("appointmentId", id))
            6 -> transaction.replace(R.id.content_flayout, AppointmentCreate1Fragment())
            7 -> transaction.replace(R.id.content_flayout, AppointmentCreate2Fragment())
                .addToBackStack(null)
            8 -> transaction.replace(R.id.content_flayout, AppointmentCreate3Fragment())
                .addToBackStack(null)
            9 -> transaction.replace(R.id.content_flayout, MapFragment())
            10 -> transaction.replace(R.id.content_flayout, PwdEditFragment())
        }
        transaction.commit()
    }
}