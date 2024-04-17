package com.map.mapbox.Ex1.Data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem

@Dao
interface ShopingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("SELECT  * FROM shopping_items")
    fun getAllItems():LiveData<List<ShoppingItem>>
}