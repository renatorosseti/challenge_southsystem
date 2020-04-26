package com.rosseti.southevents.main.model

import com.squareup.moshi.Json

data class Event (
    @Json(name = "people")
    private val people: List<Person>?,

    @Json(name = "date")
    private val date: Double?,

    @Json(name = "description")
    private val description: String?,

    @Json(name = "image")
    private val image: String?,

    @Json(name = "longitude")
    private val longitude: Double?,

    @Json(name = "latitude")
    private val latitude: Double?,

    @Json(name = "price")
    private val price: Double?,

    @Json(name = "title")
    private val title: String?,

    @Json(name = "id")
    private val id: String?,

    @Json(name = "cupons")
    private val coupons: List<Coupon>?
)

class Person {
    @Json(name = "id")
    private val id: String? = null

    @Json(name = "eventId")
    private val eventId: String? = null

    @Json(name = "name")
    private val name: String? = null

    @Json(name = "picture")
    private val picture: String? = null
}

class Coupon {
    @Json(name = "id")
    private val id: String? = null

    @Json(name = "eventId")
    private val eventId: String? = null

    @Json(name = "discount")
    private val discount: Int? = null
}