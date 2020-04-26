package com.rosseti.southevents.main.events

import com.rosseti.southevents.main.model.Event

sealed class EventsViewState {
    object ShowLoadingState: EventsViewState()
    data class ShowContentFeed(val events: List<Event>): EventsViewState()
    data class ShowRequestError(val message: Int): EventsViewState()
}