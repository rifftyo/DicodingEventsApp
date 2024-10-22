package com.rifftyo.dicodingeventsapp.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifftyo.dicodingeventsapp.data.remote.response.Event
import com.rifftyo.dicodingeventsapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailEventsViewModel: ViewModel() {
    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailEvent(context: Context, id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDetailEvents(id)
                _eventDetail.value = response.event
            } catch (e: HttpException) {
                Log.e(TAG, "onFailure: ${e.message}")
                Toast.makeText(context, "Data Gagal di Tampilkan", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                Toast.makeText(context, "Data Gagal di Tampilkan", Toast.LENGTH_SHORT).show()
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "DetailEventsViewModel"
    }
}
