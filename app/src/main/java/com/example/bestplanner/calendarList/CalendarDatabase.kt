package com.example.bestplanner.calendarList

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bestplanner.calendarList.TimeItem

@Database(entities = [TimeItem::class], version = 1)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun timeItemDao(): TimeItemDAO?
}
