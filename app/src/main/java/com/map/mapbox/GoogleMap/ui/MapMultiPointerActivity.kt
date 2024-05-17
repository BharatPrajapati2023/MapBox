package com.map.mapbox.GoogleMap.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.map.mapbox.R

class MapMultiPointerActivity : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var map:GoogleMap

    val mahara=LatLng(18.80221454,74.52341541)
    val up=LatLng(26.80221454,80.52341541)
    val delhi=LatLng(28.68221454,77.18341541)
    val ass=LatLng(26.20021454,92.902341541)
    val bih=LatLng(27.027221454,85.25241541)

    private var lofationList:ArrayList<LatLng>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map_multi_pointer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val map=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        lofationList= ArrayList()
        lofationList!!.add(mahara)
        lofationList!!.add(up)
        lofationList!!.add(delhi)
        lofationList!!.add(ass)
        lofationList!!.add(bih)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap

        for (i in lofationList!!.indices){

            map.addMarker(MarkerOptions().position(lofationList!![i]).title("Marker"))
            map.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
            map.moveCamera(CameraUpdateFactory.newLatLng(lofationList!!.get(i)))

        }
    }
}