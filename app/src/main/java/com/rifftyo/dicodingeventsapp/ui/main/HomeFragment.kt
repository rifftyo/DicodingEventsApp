package com.rifftyo.dicodingeventsapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rifftyo.dicodingeventsapp.data.remote.response.ListEventsItem
import com.rifftyo.dicodingeventsapp.databinding.FragmentHomeBinding
import com.rifftyo.dicodingeventsapp.ui.adapter.EventsAdapter
import com.rifftyo.dicodingeventsapp.ui.viewmodel.DicodingEventsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val dicodingEventsViewModel by viewModels<DicodingEventsViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManagerHorizontal = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeUpComing.layoutManager = layoutManagerHorizontal

        val layoutManagerVertical = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvHomeFinished.layoutManager = layoutManagerVertical

        if (dicodingEventsViewModel.listEventUpComing.value.isNullOrEmpty()) {
            dicodingEventsViewModel.getDicodingEvents(requireContext(),1)
        }

        if (dicodingEventsViewModel.listEventFinished.value.isNullOrEmpty()) {
            dicodingEventsViewModel.getDicodingEvents(requireContext(),0)
        }

        dicodingEventsViewModel.listEventUpComing.observe(viewLifecycleOwner) { listEventUpComing ->
            setEventsDataUpComing(listEventUpComing)
        }

        dicodingEventsViewModel.listEventFinished.observe(viewLifecycleOwner) { listEventFinished ->
            setEventsDataFinished(listEventFinished.take(5))
        }

        dicodingEventsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setEventsDataUpComing(listEventsData: List<ListEventsItem>) {
        val adapter = EventsAdapter(listEventsData)
        binding.rvHomeUpComing.adapter = adapter
    }

    private fun setEventsDataFinished(listEventsData: List<ListEventsItem>) {
        val adapter = EventsAdapter(listEventsData)
        binding.rvHomeFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}