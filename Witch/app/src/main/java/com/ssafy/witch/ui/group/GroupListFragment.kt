package com.ssafy.witch.ui.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.GroupListItem
import com.ssafy.witch.data.model.dto.HomeAppointment
import com.ssafy.witch.databinding.FragmentGroupListBinding
import com.ssafy.witch.databinding.FragmentHomeBinding
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.home.HomeListAdapter
import java.util.Calendar

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::bind, R.layout.fragment_group_list) {
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var groupList: List<GroupListItem>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.groupListFgIbAddGroup.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, GroupEditFragment()).addToBackStack("").commit()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initAdapter(){
        groupList = listOf(
            GroupListItem(1, "맛집탐방", "", GroupListItem.GroupLeader(1,"남수정",""), 5, ""),
            GroupListItem(2, "D211", "", GroupListItem.GroupLeader(2,"김덕윤",""), 1, ""),
            GroupListItem(3, "멋쟁이 알고리즘", "", GroupListItem.GroupLeader(3,"권경탁",""), 2, ""),
            GroupListItem(4, "알고리즘", "", GroupListItem.GroupLeader(4,"채용수",""), 3, ""),
            GroupListItem(5, "멋쟁이", "", GroupListItem.GroupLeader(5,"태성원",""), 2, "")
        )
        binding.groupListFgRvGroupList.adapter = GroupListAdapter(groupList) { id ->
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, GroupFragment()).commit()
        }
    }

}