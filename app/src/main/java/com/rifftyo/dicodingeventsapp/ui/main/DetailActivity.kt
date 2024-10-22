package com.rifftyo.dicodingeventsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rifftyo.dicodingeventsapp.R
import com.rifftyo.dicodingeventsapp.data.local.entity.Event
import com.rifftyo.dicodingeventsapp.databinding.ActivityDetailBinding
import com.rifftyo.dicodingeventsapp.ui.viewmodel.DetailEventsViewModel
import com.rifftyo.dicodingeventsapp.ui.viewmodel.EventDatabaseViewModel
import com.rifftyo.dicodingeventsapp.ui.viewmodel.ViewModelFactory
import com.rifftyo.dicodingeventsapp.utils.SettingPreferences
import com.rifftyo.dicodingeventsapp.utils.dataStore

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private var event: Event? = null
    private var isFavorite = true
    private val detailEventsViewModel by viewModels<DetailEventsViewModel>()

    private lateinit var eventDatabaseViewModel: EventDatabaseViewModel

    companion object {
        const val EVENT_DETAIL = "event_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventDatabaseViewModel = obtainViewModel(this@DetailActivity)

        detailEventsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val id = intent.getIntExtra(EVENT_DETAIL, 0)
        detailEventsViewModel.getDetailEvent(this, id)

        eventDatabaseViewModel.getEventById(id).observe(this) { eventFromDb ->
            if (eventFromDb != null) {
                event = eventFromDb
                isFavorite = eventFromDb.isFavorite
                updateFavoriteIcon(isFavorite)
            }
        }

        detailEventsViewModel.eventDetail.observe(this) { eventDetail ->
            if (event == null) {
                event = Event(
                    title = eventDetail.name,
                    isFavorite = false,
                    id = eventDetail.id,
                    imageEvent = eventDetail.mediaCover
                )
                eventDatabaseViewModel.insert(event as Event)
            }
            val quota = eventDetail.quota - eventDetail.registrants
            with(binding) {
                tvDetailTitle.text = eventDetail.name
                tvDetailOwn.text = eventDetail.ownerName
                tvDetailQuota.text = quota.toString()
                tvDetailBeginTime.text = eventDetail.beginTime
                tvDetailEndTime.text = eventDetail.endTime
                tvDetailDescription.text = HtmlCompat.fromHtml(
                    eventDetail.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            Glide.with(this)
                .load(eventDetail.mediaCover)
                .into(binding.imgDetailPhoto)
        }

        binding.btnRegister.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnRegister.id -> {
                val intentUrl = Intent(Intent.ACTION_VIEW).apply {
                    data = detailEventsViewModel.eventDetail.value?.link?.toUri()
                }
                startActivity(intentUrl)
            }
            binding.fabFavorite.id -> {
                event?.let { currentEvent ->
                    currentEvent.isFavorite = !currentEvent.isFavorite
                    isFavorite = currentEvent.isFavorite

                    eventDatabaseViewModel.update(currentEvent)
                    updateFavoriteIcon(isFavorite)

                    if (isFavorite) {
                        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): EventDatabaseViewModel {
        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[EventDatabaseViewModel::class.java]
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

}