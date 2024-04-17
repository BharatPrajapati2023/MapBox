package com.map.mapbox.Ex2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.map.mapbox.Ex2.Database.Article
import com.map.mapbox.R

class NewsAdapter(/*list: List<Article>*/) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_article_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.image)
            holder.title.text = article.title
            holder.publishAt.text = article.publishAt
            holder.descr.text = article.description
            holder.sourceName.text = article.source.name
            setOnItemClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: AppCompatTextView
        var publishAt: AppCompatTextView
        var descr: AppCompatTextView
        var sourceName: AppCompatTextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            descr = itemView.findViewById(R.id.description)
            publishAt = itemView.findViewById(R.id.publish_at)
            sourceName = itemView.findViewById(R.id.source)

        }

    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}