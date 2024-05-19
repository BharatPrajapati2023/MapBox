package com.map.mapbox.LiveInternetCheck

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.map.mapbox.R

class NetworkCheckActivity : AppCompatActivity() {

    // private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var cId: ConnectionLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // connectivityObserver = NetworkConnectionObserve(applicationContext)

        setContentView(R.layout.activity_network_check)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //  val status = connectivityObserver.observe().collect()
        checkNetworkConnection()

    }

    private fun checkNetworkConnection() {
        cId = ConnectionLiveData(application)
        cId.observe(this, Observer { isConnected ->
            if (isConnected) {
                Toast.makeText(applicationContext, "Network Avaliable", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Network Gone", Toast.LENGTH_SHORT).show()
            }
        })
    }
}