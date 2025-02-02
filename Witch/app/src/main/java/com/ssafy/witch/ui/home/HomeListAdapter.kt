package com.ssafy.witch.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import kotlin.math.abs

class HomeListAdapter(val appointmentList: List<MyAppointmentResponse.Appointment>,val itemClickListener:ItemClickListener) : RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeAppointmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmentList.size

    fun interface ItemClickListener {
        fun onItemClick(id: String)
    }

    inner class HomeViewHolder(private val binding: HomeAppointmentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(position: Int) {
            binding.homeLiTvAppointmentGroup.text=appointmentList[position].groupName
            binding.homeLiTvAppointmentTime.text= appointmentList[position].appointmentTime.hour.toString()+"시 "+ appointmentList[position].appointmentTime.minute.toString() + "분"
            binding.homeLiTvAppointmentName.text=appointmentList[position].appointmentName

            binding.homeAppointmentListItem.setOnClickListener {
                itemClickListener.onItemClick(appointmentList[position].appointmentId)
            }
        }
    }
}
