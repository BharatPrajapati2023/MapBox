package com.map.mapbox.Ex1.Data.repostories

import com.map.mapbox.Ex1.Data.db.ShopingDatabase
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem

class ShopingRepostory(private val db: ShopingDatabase) {
suspend fun upsert(item: ShoppingItem)=db.getShopingDao().upsert(item)
    suspend fun delete(item: ShoppingItem)=db.getShopingDao().delete(item)
    fun getAllShopingItem()=db.getShopingDao().getAllItems()
}