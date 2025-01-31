package com.ssafy.witch.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.databinding.AppointmentDetailParticipantsItemBinding

class AppointmentDetailParticipantsAdapter(val participantsList: List<AppointmentDetailItem.Participants>)
    : RecyclerView.Adapter<AppointmentDetailParticipantsAdapter.ParticipantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        val binding = AppointmentDetailParticipantsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = participantsList.size

    inner class ParticipantsViewHolder(private val binding: AppointmentDetailParticipantsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.appointmentDetailParticipantName.text= participantsList[position].nickname
        }
    }

}