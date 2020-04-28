package com.rosseti.southevents.main.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rosseti.southevents.R
import com.rosseti.southevents.main.model.Event
import kotlinx.android.synthetic.main.content_item.view.*

class EventsAdapter(
    private val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    var onItemClick: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(events[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(events[adapterPosition])
            }
        }

        fun bind(event: Event) {
            Glide.with(itemView)
                .load(event.image)
                .centerCrop()
                .apply(RequestOptions().placeholder(R.mipmap.ic_event_foreground))
                .into(itemView.contentImageView)
        }
    }
}