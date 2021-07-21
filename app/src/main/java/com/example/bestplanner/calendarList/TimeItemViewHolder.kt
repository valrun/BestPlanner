package com.example.bestplanner.calendarList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bestplanner.calendarList.TimeItem
import com.example.bestplanner.MyApp
import kotlinx.android.synthetic.main.time_item.view.*
import kotlinx.coroutines.CoroutineScope

class TimeItemViewHolder(private val root: View, private val scope : CoroutineScope) : RecyclerView.ViewHolder(root) {
    fun bind(item: TimeItem) {
        with(root) {
            title.text = item.title
            time.text = item.startTime.toString()
        }
    }
}