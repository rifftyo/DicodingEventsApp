package com.rifftyo.dicodingeventsapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rifftyo.dicodingeventsapp.data.local.entity.Event
import com.rifftyo.dicodingeventsapp.databinding.ItemUpcomingBinding
import com.rifftyo.dicodingeventsapp.ui.main.DetailActivity
import com.rifftyo.dicodingeventsapp.utils.FavoriteDiffCallback

class FavoriteAdapter(private var listEventsFavorite: List<Event>): RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUpcomingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val event = listEventsFavorite[position]
        holder.binding.tvTitleItemEvent.text = event.title
        Glide.with(holder.itemView.context)
            .load(event.imageEvent)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EVENT_DETAIL, event.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listEventsFavorite.size

    fun updateData(newList: List<Event>) {
        val diffCallback = FavoriteDiffCallback(listEventsFavorite, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listEventsFavorite = newList
        diffResult.dispatchUpdatesTo(this)
    }
}