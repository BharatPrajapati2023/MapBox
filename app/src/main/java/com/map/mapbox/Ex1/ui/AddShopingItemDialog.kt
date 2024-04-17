package com.map.mapbox.Ex1.ui

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatTextView
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem
import com.map.mapbox.R

class AddShopingItemDialog(context: Context,var addDialogListner: AddDialogListner) : AppCompatDialog(context) {
    private var addItems: AppCompatTextView? = null
    private var remove: AppCompatTextView? = null
    private var names: EditText? = null
    private var amount: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.row_add_items)
        names = findViewById<EditText>(R.id.name)
        amount = findViewById<EditText>(R.id.amount)
        addItems = findViewById<AppCompatTextView>(R.id.add_items)
        remove = findViewById<AppCompatTextView>(R.id.cancel)

        addItems?.setOnClickListener {
            val name = names?.text.toString()
            val amt = amount?.text.toString()
            if (name.isEmpty() || amt.isEmpty()){
                Toast.makeText(context,"Please Enter Informations",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val item= ShoppingItem(name,amt.toInt())
            addDialogListner.onAddButtonClicked(item)
            dismiss()
        }
        remove?.setOnClickListener{
            cancel()
        }
    }
}