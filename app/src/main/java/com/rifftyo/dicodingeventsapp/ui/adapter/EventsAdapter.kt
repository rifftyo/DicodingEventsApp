package com.rifftyo.dicodingeventsapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rifftyo.dicodingeventsapp.data.remote.response.ListEventsItem
import com.rifftyo.dicodingeventsapp.databinding.ItemUpcomingBinding
import com.rifftyo.dicodingeventsapp.ui.main.DetailActivity
import com.rifftyo.dicodingeventsapp.utils.EventsDiffCallback

class EventsAdapter(private var listEventsUpComing: List<ListEventsItem>): RecyclerView.Adapter<EventsAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUpcomingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val event = listEventsUpComing[position]
        holder.binding.tvTitleItemEvent.text = event.name
        Glide.with(holder.itemView.context)
            .load(event.imageLogo)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EVENT_DETAIL, event.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listEventsUpComing.size

    fun updateData(newList: List<ListEventsItem>) {
        val diffCallback = EventsDiffCallback(listEventsUpComing, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listEventsUpComing = newList
        diffResult.dispatchUpdatesTo(this)
    }
}