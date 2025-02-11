package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.databinding.GroupAppointmentListItemBinding
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import com.ssafy.witch.util.TimeConverter
import kotlin.math.abs

class AppointmentListAdapter(val appointmentList: List<MyAppointment>, val itemClickListener:ItemClickListener) : RecyclerView.Adapter<AppointmentListAdapter.AppointmentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentListViewHolder {
        val binding = GroupAppointmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmentList.size

    fun interface ItemClickListener {
        fun onItemClick(id: String)
    }

    inner class AppointmentListViewHolder(private val binding: GroupAppointmentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(position: Int) {
            if (appointmentList[position].isMyAppointment) {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.VISIBLE
            } else {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.GONE
            }
            binding.groupAppointmentLiTvTitle.text = appointmentList[position].name

            val time= TimeConverter().convertToLocalDateTime(appointmentList[position].appointmentTime)

            binding.groupAppointmentLiTvDate.text = time.year.toString() + "-" +time.monthValue.toString() + "-" + time.dayOfMonth.toString()
            binding.groupAppointmentLiTvTime.text =time.hour.toString() + ":" +time.minute.toString()


            binding.groupAppointmentLiTvAppointmentActive.text =
                when(appointmentList[position].status){
                    "ONGOING" -> "진행중"
                    "SCHEDULED" -> "예정"
                    "FINISHED" -> "종료"
                    else -> ""
                }


            binding.groupAppointmentLiTvAppointmentActive.setBackgroundColor(
                when(appointmentList[position].status){
                    "ONGOING" -> binding.root.context.getColor(android.R.color.holo_red_light)
                    "SCHEDULED" -> binding.root.context.getColor(android.R.color.holo_green_light)
                    "FINISHED" -> binding.root.context.getColor(android.R.color.darker_gray)
                    else -> binding.root.context.getColor(android.R.color.darker_gray)
                }
)


            binding.groupAppointmentListItem.setOnClickListener {
                itemClickListener.onItemClick(appointmentList[position].appointmentId)
            }
        }
    }
}
