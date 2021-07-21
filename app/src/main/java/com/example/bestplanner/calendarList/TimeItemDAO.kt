package com.example.bestplanner.calendarList

import androidx.room.*
import com.example.bestplanner.calendarList.TimeItem

@Dao
interface TimeItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<TimeItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(items: TimeItem)

    @Query("DELETE FROM TimeItem where ID=:id")
    suspend fun deleteByID(id: Int)

    @Query("DELETE FROM TimeItem")
    suspend fun deleteAll()

    @Query("SELECT * FROM TimeItem")
    suspend fun getItems(): List<TimeItem>
}
