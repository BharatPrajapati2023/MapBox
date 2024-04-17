package com.map.mapbox.Ex1

import android.app.Application
import com.map.mapbox.Ex1.Data.db.ShopingDatabase
import com.map.mapbox.Ex1.Data.repostories.ShopingRepostory
import com.map.mapbox.Ex1.ui.shopingList.ShopingViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class shoppingApplication : Application(),KodeinAware{

    override val kodein: Kodein=Kodein.lazy {
        import(androidXModule(this@shoppingApplication))
        bind() from singleton { ShopingDatabase(instance()) }
        bind() from singleton { ShopingRepostory(instance()) }
        bind() from provider {
            ShopingViewModelFactory(instance())
        }
    }

}