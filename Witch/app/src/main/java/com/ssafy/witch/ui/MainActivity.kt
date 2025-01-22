package com.ssafy.witch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.witch.R
import com.ssafy.witch.databinding.ActivityMainBinding
import com.ssafy.witch.ui.group.GroupFragment
import com.ssafy.witch.ui.group.GroupListFragment
import com.ssafy.witch.ui.home.HomeFragment
import com.ssafy.witch.ui.mypage.MyPageFragment


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val mainBinding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.mainABn.selectedItemId = R.id.bn_home

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_flayout, HomeFragment())
            .commit()


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
//            4 -> transaction.replace(R.id.main_flayout, MapFragment())
//            5 -> transaction.add(R.id.main_flayout, //Todo)
//                .addToBackStack(null)
//            6 -> transaction.add(R.id.main_flayout, //Todo)
//                .addToBackStack(null)
        }
        transaction.commit()
    }

}