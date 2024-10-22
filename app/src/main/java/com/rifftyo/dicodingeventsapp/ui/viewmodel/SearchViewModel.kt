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

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSearchEvents(context: Context, keyword: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getSearchEvents(0, keyword)
                _isLoading.value = false
                _searchResults.value = response.listEvents
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message}")
                Toast.makeText(context, "Data Gagal di Tampilkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resetSearchResult() {
        _searchResults.value = emptyList()
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}
