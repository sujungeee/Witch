package com.ssafy.witch.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.SnackItem
import com.ssafy.witch.databinding.SnackItemBinding

class AppointmentSnackAdatper(val appointmentSnackList: List<SnackItem>, val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<AppointmentSnackAdatper.SnackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder {
        val binding = SnackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SnackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmentSnackList.size

    fun interface ItemClickListener {
        fun onItemClick(id: Int)
    }

    inner class SnackViewHolder(private val binding: SnackItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.appointmentSnackItem.setImageResource(appointmentSnackList[position].snack_image)
            binding.appointmentSnackItem.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

}