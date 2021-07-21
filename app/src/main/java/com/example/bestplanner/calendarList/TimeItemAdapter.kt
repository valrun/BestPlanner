package com.example.bestplanner.calendarList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestplanner.R
import kotlinx.coroutines.CoroutineScope

class TimeItemAdapter (
    private val items: List<TimeItem>,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<TimeItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItemViewHolder {
        return TimeItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.time_item, parent, false),
            scope
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TimeItemViewHolder, position: Int) =
        holder.bind(items[position])
}