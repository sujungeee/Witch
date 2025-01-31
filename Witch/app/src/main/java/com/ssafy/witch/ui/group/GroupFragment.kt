package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.dto.GroupApproval
import com.ssafy.witch.data.model.dto.GroupMember
import com.ssafy.witch.data.model.dto.GroupViewModel
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.databinding.DialogGroupMembersBinding
import com.ssafy.witch.databinding.FragmentGroupBinding
import com.ssafy.witch.ui.MainActivity
import java.time.LocalDateTime


class GroupFragment : BaseFragment<FragmentGroupBinding>(FragmentGroupBinding::bind, R.layout.fragment_group) {
    private val viewModel: GroupViewModel by viewModels()

    private lateinit var appointmentListAdapter: AppointmentListAdapter
    private lateinit var appointmentList: List<AppointmentListItem>

    private lateinit var groupMemberListAdapter: GroupMemberListAdapter
    private lateinit var groupMemberList: List<GroupMember>

    private lateinit var groupApprovalListAdapter: GroupApprovalListAdapter
    private lateinit var groupApprovalList: List<GroupApproval>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initAdapter()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    fun initAdapter(){
        appointmentList = listOf(
            AppointmentListItem(1, "알고리즘 스터디", LocalDateTime.now(), "APROVED"),
            AppointmentListItem(2, "맛집탐방",  LocalDateTime.now(), "APROVED"),
            AppointmentListItem(3, "멋쟁이 알고리즘",  LocalDateTime.now(), "INACTIVE"),
            AppointmentListItem(4, "알고리즘",  LocalDateTime.now(), "INACTIVE"),
            AppointmentListItem(5, "멋쟁이",  LocalDateTime.now(), "APROVED")
        )
        binding.groupFgRvAppointmentList.adapter = AppointmentListAdapter(appointmentList) { id ->
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_flayout, GroupFragment()).commit()
        }
    }


    fun initView(){

        binding.groupFgLlGroupMember.setOnClickListener {
            val dialogBinding = DialogGroupMembersBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext())
            val isGroupLeader = true
            dialog.setContentView(dialogBinding.root)

            groupMemberList = listOf(
                GroupMember("1", "남수정", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqAp9Wxo5LUydg5cOZLdpAAZvVy7p3D_EqICjh9f25C8z2wkZQS2wGGF1Ues7LnoffNTs&usqp=CAU", true),
                GroupMember("2", "김덕윤", "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/201706/23/b71449f8-e830-45a0-bb4d-7b1a328e19f2.jpg", false),
                GroupMember("3", "권경탁", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyPkxMuo6NOHcNx-aO-wOo3eyVnB2oTq-ZwA&s", false),
                GroupMember("4", "채용수", "https://newsimg-hams.hankookilbo.com/2022/10/19/7576de8e-e4f6-4827-9f17-cfefe4be052f.jpg", false),
                GroupMember("5", "태성원", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4TzKVPcjY-234LOSKvXiXIXNtElEueYgT6w&s", false)
            )


            groupApprovalList= listOf(
                GroupApproval("1", User( "1", "남수정", "")),
                GroupApproval("2", User( "2", "김덕윤", "")),
                GroupApproval("3", User( "3", "권경탁", "")),
                GroupApproval("4", User( "4", "채용수", "")),
                GroupApproval("5", User( "5", "태성원", ""))
            )

            dialogBinding.dialogGroupMembersMasterBtnClose.setOnClickListener {
                dialog.dismiss()
            }

            if(isGroupLeader){
                dialogBinding.dialogGroupMembersMasterTvTitleApproval.visibility = View.VISIBLE
                dialogBinding.dialogGroupMembersMasterTvTitleApproval.setOnClickListener {
                    dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.GONE
                    dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.VISIBLE
                }
            }




            dialogBinding.dialogGroupMembersMasterRvMembers.adapter = GroupMemberListAdapter(groupMemberList)



            dialogBinding.dialogGroupMembersMasterTvTitleApproval.setOnClickListener{
                if (dialogBinding.dialogGroupMembersMasterRvApproval.equals(viewModel.tabState)){
                    dialogBinding.dialogGroupMembersMasterTvTitleApproval.setTextColor(Color.GREEN)
                }



                dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.GONE
                dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.VISIBLE

                dialogBinding.dialogGroupMembersMasterRvApproval.adapter=GroupApprovalListAdapter(groupApprovalList, object : GroupApprovalListAdapter.OnItemClickListener {
                    override fun onApprove(id: String) {
                        // 승인
                    }

                    override fun onReject(id: String) {
                        // 거절
                    }
                })
            }

            dialogBinding.dialogGroupMembersMasterTvTitleMember.setOnClickListener{
                dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.VISIBLE
                dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.GONE

                dialogBinding.dialogGroupMembersMasterRvApproval.adapter=GroupMemberListAdapter(groupMemberList)
            }



            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
             }
    }

}