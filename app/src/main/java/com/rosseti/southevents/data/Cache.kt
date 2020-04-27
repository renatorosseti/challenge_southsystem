package com.rosseti.southevents.data

import com.rosseti.southevents.main.model.Event

object Cache {

    var contentFeed = listOf<Event>()

    lateinit var contendDetail: Event
}
