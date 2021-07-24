package com.example.bestplanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestplanner.ComplementaryApp.Companion.instance
import com.example.bestplanner.R
import com.example.bestplanner.calendarList.TimeItemAdapter

class CalendarFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        recyclerView = view.findViewById(R.id.CalendarRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(inflater.context)
        recyclerView.adapter = TimeItemAdapter(instance.listItems, instance.getScope())

        instance.mAdapter = recyclerView.adapter as TimeItemAdapter?
//        instance.textView = findViewById(R.id.load)
        instance.loadItems()

        return view
    }
}