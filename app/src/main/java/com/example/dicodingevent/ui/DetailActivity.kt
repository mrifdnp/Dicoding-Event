package com.example.dicodingevent.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.ui.detail.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            observeViewModel()
            viewModel.fetchEventDetails(eventId)
        } else {
            showError("Invalid Event ID: $eventId")
        }
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this, Observer { event ->
            bindEventDetails(event)
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                showError(it)
                viewModel.clearErrorMessage()
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventDetails(event: Event) {
        binding.tvEventName.text = event.name
        binding.tvEventCity.text = event.cityName
        binding.tvBeginTime.text = event.beginTime
        binding.tvEndTime.text = event.endTime
        binding.tvEventDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivEventImage)

        binding.tvOwnerName.text = event.ownerName
        binding.tvRemainingQuota.text = "Remaining Quota: ${event.quota - event.registrants}"

        binding.btnOpenLink.text = "Open Event Link"
        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }
}
