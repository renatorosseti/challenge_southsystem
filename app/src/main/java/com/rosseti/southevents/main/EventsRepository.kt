package com.rosseti.southevents.main

import com.rosseti.southevents.main.model.AppRxSchedulers
import io.reactivex.Single

class EventsRepository(
    private val rxSchedulers: AppRxSchedulers
) {

    fun loadEvents(): Single<List<String>> {
        return Single.just(listOf("Item event."))
    }
}