package com.map.mapbox.Ex2.Fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.map.mapbox.Ex2.Adapter.NewsAdapter
import com.map.mapbox.Ex2.Api.Constent
import com.map.mapbox.Ex2.Api.Resource
import com.map.mapbox.Ex2.Data.ViewModel.NewsViewModel
import com.map.mapbox.Ex2.NewsActivity
import com.map.mapbox.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    val TAG = "BreakingNewsFragment"
    lateinit var newsAdapter: NewsAdapter
    lateinit var rvSearchNews: RecyclerView
    lateinit var etSearch:EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSearchNews = view.findViewById(R.id.rvSearchNews)
        etSearch=view.findViewById(R.id.etSearch)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,bundle
            )
        }

        var job:Job?=null
        etSearch.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(Constent.SEARCH_NEWS_TIME_DELY)

                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.breakingNews.observe(viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressbar()
                        response.data?.let { newsResponse ->
                            newsAdapter.differ.submitList(newsResponse.articles)

                        }
                    }

                    is Resource.Error -> {
                        hideProgressbar()
                        response.message?.let { message ->
                            Log.e(TAG, "an Error occured: $message")
                        }
                    }

                    is Resource.Loading -> {
                        showProgressbar()
                    }
                }
            })
    }

    private fun hideProgressbar() {
        //paginationProgressbar.visibility=View.INVISIBLE
    }

    private fun showProgressbar() {
        //paginationProgressbar.visibility=View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.layoutManager = LinearLayoutManager(activity)
        rvSearchNews.adapter = newsAdapter
        /*rvBreakingNews.apply(
            rvBreakingNews.adapter=newsAdapter
                    la= LinearLayoutManager (activity)
        )*/
    }
}