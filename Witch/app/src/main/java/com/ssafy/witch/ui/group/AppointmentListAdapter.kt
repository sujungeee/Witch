package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.dto.GroupListItem
import com.ssafy.witch.databinding.GroupAppointmentListItemBinding
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import kotlin.math.abs

class AppointmentListAdapter(val appointmenrList: List<AppointmentListItem>, val itemClickListener:ItemClickListener) : RecyclerView.Adapter<AppointmentListAdapter.AppointmentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentListViewHolder {
        val binding = GroupAppointmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmenrList.size

    fun interface ItemClickListener {
        fun onItemClick(id: Int)
    }

    inner class AppointmentListViewHolder(private val binding: GroupAppointmentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(position: Int) {
            if (appointmenrList[position].appointmentStatus == "APROVED") {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.VISIBLE
            } else {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.GONE
            }
            binding.groupAppointmentLiTvTitle.text = appointmenrList[position].appointmentName

            binding.groupAppointmentLiTvDate.text = appointmenrList[position].appointmentTime.year.toString() + "-" + appointmenrList[position].appointmentTime.monthValue.toString() + "-" + appointmenrList[position].appointmentTime.dayOfMonth.toString()
            binding.groupAppointmentLiTvTime.text = appointmenrList[position].appointmentTime.hour.toString() + ":" + appointmenrList[position].appointmentTime.minute.toString()




            binding.groupAppointmentListItem.setOnClickListener {
                itemClickListener.onItemClick(appointmenrList[position].appointmentId)
            }
        }
    }
}
