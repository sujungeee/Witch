package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.AppointmentListItem
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.databinding.GroupAppointmentListItemBinding
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import com.ssafy.witch.util.TimeConverter
import kotlin.math.abs

class AppointmentListAdapter(var appointmentList: List<MyAppointment>, val itemClickListener:ItemClickListener) : RecyclerView.Adapter<AppointmentListAdapter.AppointmentListViewHolder>() {

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
            if (appointmentList[position].status == "APROVED") {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.VISIBLE
            } else {
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.GONE
            }
            binding.groupAppointmentLiTvTitle.text = appointmentList[position].name

            val time= TimeConverter().convertToLocalDateTime(appointmentList[position].appointmentTime)

            binding.groupAppointmentLiTvDate.text = time.year.toString() + "-" +time.monthValue.toString() + "-" + time.dayOfMonth.toString()
            binding.groupAppointmentLiTvTime.text =time.hour.toString() + ":" +time.minute.toString()

            if (appointmentList[position].isMyAppointment){
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.VISIBLE
            }else{
                binding.groupAppointmentLiTvIsmine.visibility = ViewGroup.GONE
            }


            binding.groupAppointmentListItem.setOnClickListener {
                itemClickListener.onItemClick(appointmentList[position].appointmentId)
            }
        }
    }

    fun updateList(list: List<MyAppointment>) {
        appointmentList = list
        notifyDataSetChanged()
    }
}
