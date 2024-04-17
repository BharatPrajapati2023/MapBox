package com.map.mapbox.Ex1.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.map.mapbox.Ex1.Data.Adapter.ShopingItemAdapter
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem
import com.map.mapbox.R
import com.map.mapbox.Ex1.ui.shopingList.ShopingViewModel
import com.map.mapbox.Ex1.ui.shopingList.ShopingViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity() : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: ShopingViewModelFactory by instance()

    /* private lateinit var fragment: Fragment;*//*replace to Mapview*//*
    private lateinit var map: MapMode*//*replace to mapboxMap*//*

    private lateinit var permissionManager: Permission
    private lateinit var orignalLocation: Location
    private var locationEngin: LocationSource? = null
    private var locationLayer: Layer? = null

*/
    lateinit var viewModel: ShopingViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var addItems: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*fragment = findViewById<Fragment>(R.id.map)
        fragment.onCreate(savedInstanceState)
          fragment.getMapAsync{map->
              fragment=map
          }*/


        /*MVVM Example*/
        recyclerView = findViewById(R.id.recycler_view)
        addItems = findViewById(R.id.add_items)

        viewModel = ViewModelProviders.of(this, factory).get(ShopingViewModel::class.java)

        val adapter = ShopingItemAdapter(listOf(), viewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.getAllShopingItem().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        addItems.setOnClickListener {
            AddShopingItemDialog(this, object : AddDialogListner {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    viewModel.upsert(item)
                }

            }).show()
        }
    }

    /*override fun onStart() {
        super.onStart()
        fragment.onStart()
    }

    override fun onResume() {
        super.onResume()
        fragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        fragment.onPause()
    }

    override fun onStop() {
        super.onStop()
        fragment.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState != null)
            fragment.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        fragment.onLowMemory()
    }*/
}