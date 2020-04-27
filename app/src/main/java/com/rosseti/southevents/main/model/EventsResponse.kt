package com.rosseti.southevents.main.model

import com.squareup.moshi.Json

data class Event (
    @Json(name = "people")
    val people: List<Person>?,

    @Json(name = "date")
    val date: Double?,

    @Json(name = "description")
    val description: String?,

    @Json(name = "image")
    val image: String,

    @Json(name = "longitude")
    val longitude: Double?,

    @Json(name = "latitude")
    val latitude: Double?,

    @Json(name = "price")
    val price: Double?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "id")
    val id: String?,

    @Json(name = "cupons")
    val coupons: List<Coupon>?
)

class Person {
    @Json(name = "id")
    val id: String? = null

    @Json(name = "eventId")
    val eventId: String? = null

    @Json(name = "name")
    val name: String? = null

    @Json(name = "picture")
    val picture: String? = null
}

class Coupon {
    @Json(name = "id")
    val id: String? = null

    @Json(name = "eventId")
    val eventId: String? = null

    @Json(name = "discount")
    val discount: Int? = null
}