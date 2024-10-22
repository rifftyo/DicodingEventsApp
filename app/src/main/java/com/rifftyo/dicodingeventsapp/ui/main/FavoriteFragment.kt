package com.rifftyo.dicodingeventsapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifftyo.dicodingeventsapp.databinding.FragmentFavoriteBinding
import com.rifftyo.dicodingeventsapp.ui.adapter.FavoriteAdapter
import com.rifftyo.dicodingeventsapp.ui.viewmodel.FavoriteEventViewModel
import com.rifftyo.dicodingeventsapp.ui.viewmodel.ViewModelFactory
import com.rifftyo.dicodingeventsapp.utils.SettingPreferences
import com.rifftyo.dicodingeventsapp.utils.dataStore

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteEventViewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        setupRecyclerView()
        getFavoriteList()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager

        adapter = FavoriteAdapter(emptyList())
        binding.rvFavorite.adapter = adapter
    }

    private fun getFavoriteList() {
        favoriteEventViewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            adapter.updateData(favoriteEvents)

            if (favoriteEvents.isEmpty()) {
                binding.tvNoFavorite.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.tvNoFavorite.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteEventViewModel {
        val pref = SettingPreferences.getInstance(activity.dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[FavoriteEventViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}