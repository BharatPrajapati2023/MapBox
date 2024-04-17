package com.map.mapbox.Ex2.Database

import androidx.room.TypeConverter
import com.map.mapbox.Ex2.Data.Source


class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}