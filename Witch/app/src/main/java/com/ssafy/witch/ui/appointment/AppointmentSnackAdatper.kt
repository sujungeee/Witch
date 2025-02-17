package com.ssafy.witch.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.witch.data.model.response.SnackResponse
import com.ssafy.witch.databinding.SnackItemBinding

class AppointmentSnackAdatper(val appointmentSnackList: List<SnackResponse.SnackInfo>, val itemClickListener: ItemClickListener)
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
        fun onItemClick(snackId: String)
    }

    inner class SnackViewHolder(private val binding: SnackItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val imageUrl = appointmentSnackList[position].snackImageUrl
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.appointmentSnackItem)

            binding.appointmentSnackItem.setOnClickListener {
                itemClickListener.onItemClick(appointmentSnackList[position].snackId)
            }
        }
    }

}