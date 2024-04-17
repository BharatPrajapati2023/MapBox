package com.map.mapbox.Ex2.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.map.mapbox.Ex2.Data.Source
import java.io.Serializable


@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var author: String,
    var content: String,
    var description: String,
    var publishAt: String,
    var source: Source,
    var title: String,
    var url: String,
    var urlToImage: String
) : Serializable
