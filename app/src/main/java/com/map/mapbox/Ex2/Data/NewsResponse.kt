package com.map.mapbox.Ex2.Data

import com.map.mapbox.Ex2.Database.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
