package com.rifftyo.dicodingeventsapp.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifftyo.dicodingeventsapp.data.remote.response.ListEventsItem
import com.rifftyo.dicodingeventsapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DicodingEventsViewModel : ViewModel() {
    private val _listEventUpComing = MutableLiveData<List<ListEventsItem>>()
    val listEventUpComing: LiveData<List<ListEventsItem>> = _listEventUpComing

    private val _listEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventFinished: LiveData<List<ListEventsItem>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDicodingEvents(context: Context, status: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvents(status)
                _isLoading.value = false
                when (status) {
                    0 -> _listEventFinished.value = response.listEvents
                    1 -> _listEventUpComing.value = response.listEvents
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message}")
                Toast.makeText(context, "Data Gagal di Tampilkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "DicodingEventsViewModel"
    }
}
