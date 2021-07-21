package com.example.bestplanner.calendarList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity()
data class TimeItem(
        @PrimaryKey() val id: Int,
        @ColumnInfo(name = "title") val title: String?,
        @ColumnInfo(name = "startTime") val startTime: Time)
