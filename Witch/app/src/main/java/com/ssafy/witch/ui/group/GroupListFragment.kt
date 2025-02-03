package com.ssafy.witch.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.GroupListItem
import com.ssafy.witch.databinding.FragmentGroupListBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::bind, R.layout.fragment_group_list) {
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var groupList: List<GroupListItem>

    private lateinit var mainActivity: MainActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        initAdapter()

        binding.groupListFgIbAddGroup.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 1)
            startActivity(contentActivity)
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