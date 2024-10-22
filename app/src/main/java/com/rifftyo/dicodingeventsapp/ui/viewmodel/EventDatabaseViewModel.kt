package com.rifftyo.dicodingeventsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifftyo.dicodingeventsapp.data.local.entity.Event
import com.rifftyo.dicodingeventsapp.data.local.room.EventRepository
import kotlinx.coroutines.launch

class EventDatabaseViewModel(application: Application): ViewModel() {
    private val mEventRepository: EventRepository = EventRepository(application)

    fun insert(event: Event) {
        viewModelScope.launch {
            mEventRepository.insert(event)
        }
    }

    fun update(event: Event) {
        viewModelScope.launch {
            mEventRepository.update(event)
        }
    }

    fun getEventById(id: Int): LiveData<Event> {
        return mEventRepository.getEventById(id)
    }
}