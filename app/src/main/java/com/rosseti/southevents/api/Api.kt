package com.rosseti.southevents.api

import com.rosseti.southevents.main.model.Event
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    companion object {
        const val URL = "https://5b840ba5db24a100142dcd8c.mockapi.io/api/"
    }

    @GET("events")
    fun fetchEvents(): Single<List<Event>>

}
