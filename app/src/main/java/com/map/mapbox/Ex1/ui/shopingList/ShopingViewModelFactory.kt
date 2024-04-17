package com.map.mapbox.Ex1.ui.shopingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.map.mapbox.Ex1.Data.repostories.ShopingRepostory

class ShopingViewModelFactory(private val repostory: ShopingRepostory) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopingViewModel(repostory) as T
    }
}