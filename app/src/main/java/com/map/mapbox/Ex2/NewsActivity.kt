package com.map.mapbox.Ex2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.map.mapbox.Ex2.Data.Repositry.NewsRepostory
import com.map.mapbox.Ex2.Data.Repositry.NewsViewModelProviderFactory
import com.map.mapbox.Ex2.Data.ViewModel.NewsViewModel
import com.map.mapbox.Ex2.Database.ArticleDatabase
import com.map.mapbox.R

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var bottomNavigationView:BottomNavigationView
    lateinit var newsNavHostFragment:Fragment

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navHostFragment.navController
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigationView=findViewById(R.id.bottomNavigationView)
        setupActionBarWithNavController(navController)

        val repostory=NewsRepostory(ArticleDatabase(this))
        val viewModelProvider=NewsViewModelProviderFactory(repostory)
        viewModel=ViewModelProvider(this,viewModelProvider).get(NewsViewModel::class.java)
    }
}



