package com.map.mapbox.Ex2.Fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.map.mapbox.Ex2.Adapter.NewsAdapter
import com.map.mapbox.Ex2.Api.Resource
import com.map.mapbox.Ex2.Data.ViewModel.NewsViewModel
import com.map.mapbox.Ex2.NewsActivity
import com.map.mapbox.R


class BreakingNewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    val TAG = "BreakingNewsFragment"
    lateinit var newsAdapter: NewsAdapter
    lateinit var rvBreakingNews: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBreakingNews = view.findViewById(R.id.rvBreakingNews)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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

                else -> {}
            }
        })

    }

    private fun hideProgressbar() {
        //paginationProgressbar.visibility=View.INVISIBLE
        Log.wtf("sdfsdfsd", "dfdfdfdf")
    }

    private fun showProgressbar() {
        //paginationProgressbar.visibility=View.VISIBLE
        Log.wtf("sdfsdfsd", "dfdfdfdf")
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.layoutManager = LinearLayoutManager(activity)
        rvBreakingNews.adapter = newsAdapter
        /*rvBreakingNews.apply(
            rvBreakingNews.adapter=newsAdapter
                    la= LinearLayoutManager (activity)
        )*/
    }

}