package com.rifftyo.dicodingeventsapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkManager(context: Context): LiveData<Boolean>() {
    override fun onActive() {
        super.onActive()
        checkNetworkConnectivity()
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }

    }

    private fun checkNetworkConnectivity() {
        val network = connectivityManager.activeNetwork
        if (network == null) {
            postValue(false)
        }
        val requestBuilder = NetworkRequest.Builder().apply {
            addCapability(NET_CAPABILITY_INTERNET)
            addCapability(NET_CAPABILITY_VALIDATED)
            addTransportType(TRANSPORT_CELLULAR)
            addTransportType(TRANSPORT_WIFI)
            addTransportType(TRANSPORT_ETHERNET)
        }

        connectivityManager.registerNetworkCallback(requestBuilder.build(), networkCallback)
    }

}