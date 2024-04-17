package com.map.mapbox.Ex1.ui

import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem

interface AddDialogListner {
    fun onAddButtonClicked(item: ShoppingItem)
}