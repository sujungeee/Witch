package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.response.GroupApproval
import com.ssafy.witch.data.model.dto.GroupMember
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.databinding.DialogGroupMembersBinding
import com.ssafy.witch.databinding.FragmentGroupBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.appointment.MapFragment
import java.time.LocalDateTime
import kotlin.properties.Delegates


class GroupFragment : BaseFragment<FragmentGroupBinding>(FragmentGroupBinding::bind, R.layout.fragment_group) {
    private val viewModel: GroupViewModel by viewModels()

    private lateinit var appointmentListAdapter: AppointmentListAdapter
    private lateinit var appointmentList: List<AppointmentListItem>

    private lateinit var groupMemberListAdapter: GroupMemberListAdapter
    private lateinit var groupMemberList: List<GroupMember>

    private lateinit var groupApprovalListAdapter: GroupApprovalListAdapter
    private lateinit var groupApprovalList: List<GroupApproval>

    private lateinit var dialogBinding: DialogGroupMembersBinding

    private lateinit var mainActivity: MainActivity


    private var isGroupLeader by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        isGroupLeader=true
        initView()
        initAdapter()

        binding.groupFgIvAppointmentCreate.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 6)
            startActivity(contentActivity)
        }

        binding.groupFgIbGroupEdit.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 2)
            startActivity(contentActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(){
        distinctGroupLeader()
        initMemberDialog()
        initOutDialog()
        binding.groupFgIbGroupEdit.setOnClickListener {
            mainActivity.openFragment(7)
        }

    }

    fun distinctGroupLeader(){
        if(isGroupLeader){
            binding.groupFgIbGroupEdit.visibility=View.VISIBLE
            binding.groupFgTvDoor.text="모임 삭제"
        }
        else{
            binding.groupFgIbGroupEdit.visibility=View.GONE
            binding.groupFgTvDoor.text="모임 나가기"
        }
    }

    fun initOutDialog(){
        binding.groupFgLlDoor.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_group_out)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.findViewById<View>(R.id.group_out_dl_btn_no).setOnClickListener {
                dialog.dismiss()
            }

            if (isGroupLeader) {
                dialog.findViewById<TextView>(R.id.group_out_dl_tv_title).text="모임을 삭제하시겠습니까?"
                dialog.findViewById<View>(R.id.group_out_dl_btn_yes).setOnClickListener {
                    viewModel.deleteGroup()
                    mainActivity.openFragment(2)
                    dialog.dismiss()
                }
            }
            else {
                dialog.findViewById<TextView>(R.id.group_out_dl_tv_title).text="모임을 나가시겠습니까?"
                dialog.findViewById<View>(R.id.group_out_dl_btn_yes).setOnClickListener {
                    viewModel.groupOut()
                    mainActivity.openFragment(2)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    fun initMemberDialog(){
        dialogBinding=DialogGroupMembersBinding.inflate(layoutInflater)

        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        binding.groupFgLlGroupMember.setOnClickListener {

            dialogBinding.dialogGroupMembersMasterBtnClose.setOnClickListener {
                dialog.dismiss()
            }

            if(isGroupLeader){
                dialogBinding.dialogGroupMembersMasterVApproval.visibility = View.VISIBLE
                dialogBinding.dialogGroupMembersMasterTvTitleApproval.visibility = View.VISIBLE
                dialogBinding.dialogGroupMembersMasterTvTitleApproval.setOnClickListener {
                    dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.GONE
                    dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.VISIBLE
                }
            }

            dialogBinding.dialogGroupMembersMasterTvTitleApproval.setOnClickListener{
                viewModel.setTabState("APPROVAL")
            }

            dialogBinding.dialogGroupMembersMasterTvTitleMember.setOnClickListener{
                viewModel.setTabState("MEMBER")
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
        }

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
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            contentActivity.putExtra("openFragment", 9)
            startActivity(contentActivity)
        }

        viewModel.tabState.observe(viewLifecycleOwner, {
            when(it){
                "APPROVAL" -> {
                    groupApprovalList= listOf(
                        GroupApproval("1", User( "1", "dkdkdk@naver.com","남dasdasdasㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇ수정", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4TzKVPcjY-234LOSKvXiXIXNtElEueYgT6w&s")),
                        GroupApproval("2", User( "2", "dkdkdk@naver.com","김덕윤", "")),
                        GroupApproval("3", User( "3", "dkdkdk@naver.com","권경탁", "")),
                        GroupApproval("4", User( "4", "dkdkdk@naver.com","채용수", "")),
                        GroupApproval("5", User( "5", "dkdkdk@naver.com","태성원", ""))
                    )


                    dialogBinding.dialogGroupMembersMasterRvApproval.adapter=GroupApprovalListAdapter(groupApprovalList, object : GroupApprovalListAdapter.OnItemClickListener {
                        override fun onApprove(id: String) {
                            // 승인
                        }

                        override fun onReject(id: String) {
                            // 거절
                        }
                    })
                    dialogBinding.dialogGroupMembersMasterVMember.setBackgroundColor(resources.getColor(R.color.witch_gray))
                    dialogBinding.dialogGroupMembersMasterVApproval.setBackgroundColor(resources.getColor(R.color.witch_green))
                    dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.GONE
                    dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.VISIBLE
                    dialogBinding.dialogGroupMembersMasterTvTitleApproval.setTextColor(resources.getColor(R.color.witch_green))
                    dialogBinding.dialogGroupMembersMasterTvTitleMember.setTextColor(resources.getColor(R.color.witch_white))
                }
                "MEMBER" -> {
                    groupMemberList = listOf(
                        GroupMember("1", "남수정", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqAp9Wxo5LUydg5cOZLdpAAZvVy7p3D_EqICjh9f25C8z2wkZQS2wGGF1Ues7LnoffNTs&usqp=CAU", true),
                        GroupMember("2", "김덕윤", "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/201706/23/b71449f8-e830-45a0-bb4d-7b1a328e19f2.jpg", false),
                        GroupMember("3", "권경탁", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyPkxMuo6NOHcNx-aO-wOo3eyVnB2oTq-ZwA&s", false),
                        GroupMember("4", "채용수", "https://newsimg-hams.hankookilbo.com/2022/10/19/7576de8e-e4f6-4827-9f17-cfefe4be052f.jpg", false),
                        GroupMember("5", "태성원", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4TzKVPcjY-234LOSKvXiXIXNtElEueYgT6w&s", false)
                    )

                    dialogBinding.dialogGroupMembersMasterRvMembers.adapter = GroupMemberListAdapter(groupMemberList)
                    dialogBinding.dialogGroupMembersMasterVMember.setBackgroundColor(resources.getColor(R.color.witch_green))
                    dialogBinding.dialogGroupMembersMasterVApproval.setBackgroundColor(resources.getColor(R.color.witch_gray))

                    dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.VISIBLE
                    dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.GONE
                    dialogBinding.dialogGroupMembersMasterTvTitleApproval.setTextColor(resources.getColor(R.color.witch_white))
                    dialogBinding.dialogGroupMembersMasterTvTitleMember.setTextColor(resources.getColor(R.color.witch_green))
                }
            }
        }
        )
    }

}