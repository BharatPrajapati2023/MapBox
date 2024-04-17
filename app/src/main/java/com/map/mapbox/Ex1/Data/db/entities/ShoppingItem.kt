package com.map.mapbox.Ex1.Data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @ColumnInfo(name = "item_name")
    var name: String,
    @ColumnInfo(name = "iem_amount")
    var amount: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}