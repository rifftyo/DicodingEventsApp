package com.rifftyo.dicodingeventsapp.data.remote.retrofit

import com.rifftyo.dicodingeventsapp.data.remote.response.DicodingEventsResponse
import com.rifftyo.dicodingeventsapp.data.remote.response.EventsDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(@Query("active") active: Int): DicodingEventsResponse

    @GET("events")
    suspend fun getSearchEvents(
        @Query("active") active: Int = 0,
        @Query("q") keyword: String
    ): DicodingEventsResponse

    @GET("events/{id}")
    suspend fun getDetailEvents(@Path("id") id: Int): EventsDetailResponse

    @GET("events")
    suspend fun getNearestActiveEvent(
        @Query("active")active: Int = -1,
        @Query("limit")limit: Int = 1
    ) : DicodingEventsResponse
}