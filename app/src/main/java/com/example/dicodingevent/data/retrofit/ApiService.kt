package com.example.dicodingevent.data.retrofit


import com.example.dicodingevent.data.response.DetailResponse
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.EventsResponse
import com.example.dicodingevent.data.response.ListEventsItem


import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getUpcomingEvents(
        @Query("active") active: Int = 1
    ): Call<EventsResponse>

    @GET("events")
    fun getPastEvents(
        @Query("active") active: Int = 0
    ): Call<EventsResponse>


    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<DetailResponse>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int = -1, // Default to -1 to include all events
        @Query("q") query: String
    ): Call<EventsResponse>

}