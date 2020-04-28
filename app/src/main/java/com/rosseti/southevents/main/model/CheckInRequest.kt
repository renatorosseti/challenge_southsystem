package com.rosseti.southevents.main.model

import com.squareup.moshi.Json

data class CheckInRequest(
    @Json(name = "name")
    val eventId: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "name")
    val email: String
)