package com.map.mapbox.Ex2.Data.Repositry

import com.map.mapbox.Ex2.Api.ApiClient
import com.map.mapbox.Ex2.Database.ArticleDatabase

class NewsRepostory(val db: ArticleDatabase) {
    /*suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
    fun getAllArticles() = db.getArticleDao().getAllArticles()*/

    suspend fun getBreakingNews(cunteryCode: String, pageNumber: Int) =
        ApiClient.api.getBreakingNews(cunteryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        ApiClient.api.searchForNews(searchQuery, pageNumber)

}