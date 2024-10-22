package com.rifftyo.dicodingeventsapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rifftyo.dicodingeventsapp.R
import com.rifftyo.dicodingeventsapp.utils.NetworkManager

class NoConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        supportActionBar?.hide()

        val networkManager = NetworkManager(this)
        networkManager.observe(this) { isConnected ->
            if (isConnected) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}