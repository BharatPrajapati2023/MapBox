package com.map.mapbox.Ex2.Data.Repositry


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.map.mapbox.Ex2.Data.ViewModel.NewsViewModel

class NewsViewModelProviderFactory(val newsRepostory: NewsRepostory):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepostory)as T
    }
}