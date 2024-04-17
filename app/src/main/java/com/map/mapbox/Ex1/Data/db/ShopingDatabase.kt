package com.map.mapbox.Ex1.Data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShopingDatabase : RoomDatabase() {
    abstract fun getShopingDao(): ShopingDao

    companion object {
        @Volatile
        private var instance: ShopingDatabase? = null
        private val Lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(Lock){
            instance ?: createDataBase(context).also {
                instance =it
            }
        }

        private fun createDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ShopingDatabase::class.java,
            "shoppingDB.db"
        ).build()
    }
}