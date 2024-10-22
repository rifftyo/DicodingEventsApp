package com.rifftyo.dicodingeventsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rifftyo.dicodingeventsapp.data.local.entity.Event
import com.rifftyo.dicodingeventsapp.data.local.room.EventRepository

class FavoriteEventViewModel(application: Application) : ViewModel() {
    private val mEventRepository: EventRepository = EventRepository(application)

    fun getFavoriteEvents(): LiveData<List<Event>> = mEventRepository.getFavoriteEvents()
}