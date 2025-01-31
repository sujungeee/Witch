package com.ssafy.witch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.witch.R
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.ui.appointment.AppointmentCreate1Activity
import com.ssafy.witch.ui.appointment.AppointmentCreate2Activity
import com.ssafy.witch.ui.appointment.AppointmentCreate3Activity
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.mypage.MyPageFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val mainBinding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val fragmentIdx = intent.getIntExtra("moveFragment", -1)
        if (fragmentIdx != -1) {
            moveFragment(fragmentIdx)
            intent.removeExtra("moveFragment")
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_flayout, HomeFragment())
                .commit()
        }

        mainBinding.mainABn.selectedItemId = R.id.bn_home

        mainBinding.mainABn.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bn_home -> {
                    openFragment(1)
                    true
                }
                R.id.bn_group -> {
                    openFragment(2)
                    true
                }
                R.id.bn_mypage -> {
                    openFragment(3)
                    true
                }
                else -> false
            }

        }
        mainBinding.mainABn.setOnItemReselectedListener { item ->
            if(mainBinding.mainABn.selectedItemId != item.itemId){
                mainBinding.mainABn.selectedItemId = item.itemId
            }
        }

    }

    fun openFragment(index: Int) {
        moveFragment(index)
    }

    private fun moveFragment(index:Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> transaction.replace(R.id.main_flayout, HomeFragment())
            2 -> transaction.replace(R.id.main_flayout, GroupListFragment())
            3 -> transaction.replace(R.id.main_flayout, MyPageFragment())
            4 -> transaction.replace(R.id.main_flayout, SnackCreateFragment())
            6 -> transaction.replace(R.id.main_flayout, GroupFragment())
//            7 -> transaction.replace(R.id.main_flayout, AppointmentCreate1Activity())
//            8 -> transaction.replace(R.id.main_flayout, AppointmentCreate2Activity())
//                .addToBackStack(null)
//            9 -> transaction.replace(R.id.main_flayout, AppointmentCreate3Activity())
//                .addToBackStack(null)
//            5 -> transaction.add(R.id.main_flayout, //Todo)
//                .addToBackStack(null)
//            6 -> transaction.add(R.id.main_flayout, //Todo)
//                .addToBackStack(null)
        }
        transaction.commit()
    }

}