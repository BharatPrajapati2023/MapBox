package com.map.mapbox.Ex1.ui.shopingList

import androidx.lifecycle.ViewModel
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem
import com.map.mapbox.Ex1.Data.repostories.ShopingRepostory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopingViewModel(private val repostory: ShopingRepostory) : ViewModel() {

    fun upsert(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repostory.upsert(item)
    }
    fun delete(item: ShoppingItem)=CoroutineScope(Dispatchers.Main).launch {
        repostory.delete(item)
    }
    fun getAllShopingItem()=repostory.getAllShopingItem()
}