package com.rifftyo.dicodingeventsapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifftyo.dicodingeventsapp.data.remote.response.ListEventsItem
import com.rifftyo.dicodingeventsapp.databinding.FragmentUpcomingBinding
import com.rifftyo.dicodingeventsapp.ui.adapter.EventsAdapter
import com.rifftyo.dicodingeventsapp.ui.viewmodel.DicodingEventsViewModel

class UpComingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val dicodingEventsViewModel by viewModels<DicodingEventsViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding
            .inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvEventUpComing.layoutManager = layoutManager

        if (dicodingEventsViewModel.listEventUpComing.value.isNullOrEmpty()) {
            dicodingEventsViewModel.getDicodingEvents(requireContext(),1)
        }

        dicodingEventsViewModel.listEventUpComing.observe(viewLifecycleOwner) { listEventUpComing ->
            setEventsData(listEventUpComing)
        }

        dicodingEventsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventsData(listEventsData: List<ListEventsItem>) {
        val adapter = EventsAdapter(listEventsData)
        binding.rvEventUpComing.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarUpComing.visibility = View.VISIBLE
        } else {
            binding.progressBarUpComing.visibility = View.GONE
        }
    }
}