package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.GroupListItem
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import kotlin.math.abs

class GroupListAdapter(val groupList: List<GroupListItem>, val itemClickListener:ItemClickListener) : RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val binding = GroupListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = groupList.size

    fun interface ItemClickListener {
        fun onItemClick(id: Int)
    }

    inner class GroupListViewHolder(private val binding: GroupListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.groupLiTvGroupName.text = groupList[position].name
            binding.groupLiTvGroupDay.text = "함께한 지 " +groupList[position].createdAt+"일 째"
            binding.groupLiIvGroupPopulation.text = groupList[position].num_group_members.toString()
            binding.groupLiIvGroupMaster.text = groupList[position].group_leader.nickname



            binding.groupLiIbGroupEnter.setOnClickListener {
                itemClickListener.onItemClick(groupList[position].group_id)
            }
        }
    }
}
