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
import com.bumptech.glide.Glide
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.GroupApproval
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.GroupJoinListResponse.JoinRequest
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
    private lateinit var appointmentList: List<MyAppointment>

    private lateinit var groupMemberListAdapter: GroupMemberListAdapter
    private lateinit var groupMemberList: List<User>

    private lateinit var groupJoinListAdapter: GroupApprovalListAdapter
    private lateinit var groupJoinList: List<JoinRequest>

    private lateinit var dialogBinding: DialogGroupMembersBinding

    private lateinit var mainActivity: MainActivity

    private var groupId = ""


    private var isGroupLeader by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        initView()
        initAdapter()
        initObserver()

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
            contentActivity.putExtra("id", groupId)
            startActivity(contentActivity)
        }
    }



    fun initView(){

        initMemberDialog()
        initOutDialog()
        binding.groupFgIbGroupEdit.setOnClickListener {
            mainActivity.openFragment(7)
        }

        viewModel.getGroup(groupId)
        viewModel.getGroupAppointments(groupId)
    }


    fun initObserver(){
        viewModel.group.observe(viewLifecycleOwner, {
            Glide.with(binding.root)
                .load(viewModel.group.value?.groupImageUrl)
                .into(binding.groupFgIvGroupImg)
            isGroupLeader=viewModel.group.value?.isLeader!!
            distinctGroupLeader()
            binding.groupFgTvName.text = viewModel.group.value?.name
            binding.groupFgTvLateCount.text=viewModel.group.value?.cntLateArrival.toString()
        })

        viewModel.groupAppointments.observe(viewLifecycleOwner, {
            appointmentList=it
            binding.groupFgTvTotalAppointment.text=it.size.toString()
            binding.groupFgRvAppointmentList.adapter = AppointmentListAdapter(appointmentList) { id ->
                val contentActivity = Intent(requireContext(), ContentActivity::class.java)
                contentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                contentActivity.putExtra("openFragment", 9)
                startActivity(contentActivity)
            }
        })
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
                    viewModel.groupOut(groupId, mainActivity)
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
        viewModel.tabState.observe(viewLifecycleOwner, {
            when(it){
                "APPROVAL" -> {
                    viewModel.getGroupJoinList(groupId)

                    viewModel.groupJoinList.observe(viewLifecycleOwner, {
                        groupJoinList=it
                        dialogBinding.dialogGroupMembersMasterRvApproval.adapter=GroupApprovalListAdapter(groupJoinList, object : GroupApprovalListAdapter.OnItemClickListener {
                            override fun onApprove(id: String) {
                                // 승인
                                viewModel.approveJoinRequest(id)
                            }

                            override fun onReject(id: String) {
                                // 거절
                                viewModel.rejectJoinRequest(id)
                            }
                        })
                    })



                    dialogBinding.dialogGroupMembersMasterVMember.setBackgroundColor(resources.getColor(R.color.witch_gray))
                    dialogBinding.dialogGroupMembersMasterVApproval.setBackgroundColor(resources.getColor(R.color.witch_green))
                    dialogBinding.dialogGroupMembersMasterRvMembers.visibility = View.GONE
                    dialogBinding.dialogGroupMembersMasterRvApproval.visibility = View.VISIBLE
                    dialogBinding.dialogGroupMembersMasterTvTitleApproval.setTextColor(resources.getColor(R.color.witch_green))
                    dialogBinding.dialogGroupMembersMasterTvTitleMember.setTextColor(resources.getColor(R.color.witch_white))
                }
                "MEMBER" -> {
                    viewModel.getGroupMemberList(groupId)

                    viewModel.groupMember.observe(viewLifecycleOwner, {
                        groupMemberList=it
                        dialogBinding.dialogGroupMembersMasterRvMembers.adapter = GroupMemberListAdapter(groupMemberList)
                    })



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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            groupId = it.getString("groupId").toString()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            GroupFragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }

}