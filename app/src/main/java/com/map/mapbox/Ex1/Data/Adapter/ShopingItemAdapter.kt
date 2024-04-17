package com.map.mapbox.Ex1.Data.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.map.mapbox.Ex1.Data.db.entities.ShoppingItem
import com.map.mapbox.R
import com.map.mapbox.Ex1.ui.shopingList.ShopingViewModel

class ShopingItemAdapter(var items: List<ShoppingItem>, private val viewModel: ShopingViewModel):RecyclerView.Adapter<ShopingItemAdapter.ItemViewHolder> (){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.row_ahoping_item,parent,false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curShopingItem=items[position]

        holder.name.text=curShopingItem.name
        holder.amount.text= curShopingItem.amount.toString()

        holder.delete.setOnClickListener {
            viewModel.delete(curShopingItem)
        }
        holder.add.setOnClickListener {
            curShopingItem.amount++
            viewModel.upsert(curShopingItem)
        }
        holder.remove.setOnClickListener{
            if (curShopingItem.amount>0){
                curShopingItem.amount--
                viewModel.upsert(curShopingItem)
            }
        }
    }
    inner class ItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var name: TextView
        var amount: TextView
        var delete:AppCompatImageView
        var add:AppCompatImageView
        var remove:AppCompatImageView
        init{

            name=itemView.findViewById(R.id.name)
            amount=itemView.findViewById(R.id.amount)
            delete=itemView.findViewById(R.id.delete)
            add=itemView.findViewById(R.id.add)
            remove=itemView.findViewById(R.id.remove)
        }
    }
}