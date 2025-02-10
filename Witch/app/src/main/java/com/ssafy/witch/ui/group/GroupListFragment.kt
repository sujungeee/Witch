package com.ssafy.witch.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.databinding.FragmentGroupListBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::bind, R.layout.fragment_group_list) {
    private lateinit var groupListAdapter: GroupListAdapter

    private val viewModel: GroupListViewModel by viewModels()

    private lateinit var mainActivity: MainActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        initView()
        initAdapter()

        binding.groupListFgIbAddGroup.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.putExtra("openFragment", 1)
            startActivity(contentActivity)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(){
        viewModel.getGroupList()
    }

    fun initAdapter(){

        viewModel.groupList.observe(viewLifecycleOwner) {
            binding.groupListFgRvGroupList.adapter = GroupListAdapter(it) { id ->
                (requireActivity() as MainActivity).openFragment(5, id)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getGroupList()
    }

}