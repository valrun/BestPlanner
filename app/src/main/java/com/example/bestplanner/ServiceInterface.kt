package com.example.bestplanner

import com.example.bestplanner.calendarList.TimeItem
import okhttp3.ResponseBody
import retrofit2.http.*

interface ServiceInterface {
    @GET("/posts")
    suspend fun getPosts(): ArrayList<TimeItem>

    @POST("/posts")
    suspend fun addPost(@Body post: TimeItem): TimeItem?

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): ResponseBody

}