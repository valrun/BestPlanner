package com.example.bestplanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestplanner.MyApp.Companion.instance
import com.example.bestplanner.R
import com.example.bestplanner.calendarList.TimeItemAdapter

class calendarFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val scope = lifecycle.coroutineScope

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
        recyclerView.adapter = TimeItemAdapter(instance.listItems, scope)

        instance.mAdapter = recyclerView.adapter as TimeItemAdapter?
//        instance.textView = findViewById(R.id.load)
        instance.loadItems(scope)

        return view
    }
}