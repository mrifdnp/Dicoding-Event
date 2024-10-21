package com.example.dicodingevent.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class EventsAdapter : ListAdapter<ListEventsItem, EventsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("EVENT_ID", event.id) // Kirim ID sebagai int
                putExtra("EVENT_NAME", event.name) // Kirim nama event
                putExtra("EVENT_DESCRIPTION", event.description) // Kirim deskripsi event
                putExtra("EVENT_OWNER_NAME", event.ownerName) // Kirim nama pemilik)
                putExtra("EVENT_CITY", event.cityName) // Kirim nama kota
                putExtra("EVENT_BEGIN_TIME", event.beginTime) // Kirim waktu mulai
                putExtra("EVENT_END_TIME", event.endTime) // Kirim waktu selesai
                putExtra("EVENT_MEDIA_COVER", event.mediaCover) // Kirim URL gambar
                putExtra("EVENT_LINK", event.link)
            }
            context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        private val outputDateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // For displaying only time

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvEventCity.text = event.cityName

            try {

                val beginTime = inputDateFormat.parse(event.beginTime)
                val endTime = inputDateFormat.parse(event.endTime)


                binding.tvBeginTime.text = beginTime?.let { outputDateFormat.format(it) } ?: "Invalid date"

                binding.tvEndTime.text = endTime?.let { timeFormat.format(it) } ?: "Invalid date"
            } catch (e: ParseException) {

                binding.tvBeginTime.text = "Invalid date"
                binding.tvEndTime.text = "Invalid date"
            }


            Glide.with(binding.ivEventImage.context)
                .load(event.mediaCover)
                .into(binding.ivEventImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
