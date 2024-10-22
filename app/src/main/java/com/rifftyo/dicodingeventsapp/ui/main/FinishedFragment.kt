package com.rifftyo.dicodingeventsapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rifftyo.dicodingeventsapp.data.remote.response.ListEventsItem
import com.rifftyo.dicodingeventsapp.databinding.FragmentFinishedBinding
import com.rifftyo.dicodingeventsapp.ui.adapter.EventsAdapter
import com.rifftyo.dicodingeventsapp.ui.viewmodel.DicodingEventsViewModel
import com.rifftyo.dicodingeventsapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val dicodingEventsViewModel by viewModels<DicodingEventsViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
    private var searchJob: Job? = null
    private lateinit var eventsAdapter: EventsAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        if (dicodingEventsViewModel.listEventFinished.value.isNullOrEmpty()) {
            dicodingEventsViewModel.getDicodingEvents(requireContext(), 0)
        }

        dicodingEventsViewModel.listEventFinished.observe(viewLifecycleOwner) { listEventFinished ->
            if (!isSearching) {
                setEventsData(listEventFinished)
            }
        }

        searchViewModel.searchResults.observe(viewLifecycleOwner) { searchResult ->
            if (!searchResult.isNullOrEmpty()) {
                isSearching = true
                setEventsData(searchResult)
            } else {
                if (isSearching) {
                    setEventsData(emptyList())
                }
                isSearching = false
            }
        }

        dicodingEventsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setupSearchListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        binding.rvEventFinished.layoutManager = layoutManager
        eventsAdapter = EventsAdapter(emptyList())
        binding.rvEventFinished.adapter = eventsAdapter
    }

    private fun setupSearchListener() {
        binding.svSearchEvent.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svSearchEvent.clearFocus()
                if (!query.isNullOrEmpty()) {
                    isSearching = true
                    searchViewModel.getSearchEvents(requireContext(), query)
                } else {
                    resetSearchToDefault()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    if (!newText.isNullOrEmpty()) {
                        isSearching = true
                        searchViewModel.getSearchEvents(requireContext(), newText)
                    } else {
                        resetSearchToDefault()
                    }
                }
                return true
            }
        })
    }

    private fun resetSearchToDefault() {
        isSearching = false
        searchViewModel.resetSearchResult()
        dicodingEventsViewModel.listEventFinished.value?.let { setEventsData(it) }
    }

    private fun setEventsData(listEventsData: List<ListEventsItem>) {
        eventsAdapter.updateData(listEventsData)

        if (listEventsData.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvEventFinished.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvEventFinished.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
