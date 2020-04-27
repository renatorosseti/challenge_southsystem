package com.rosseti.southevents.main.events

import com.rosseti.southevents.main.model.Event
import com.rosseti.southevents.main.model.NetworkException

sealed class EventsViewState {
    object ShowLoadingState: EventsViewState()
    data class ShowContentFeed(val events: List<Event>): EventsViewState()
    data class ShowRequestError(val message: Int, var networkException: NetworkException = NetworkException(Throwable())): EventsViewState()
}