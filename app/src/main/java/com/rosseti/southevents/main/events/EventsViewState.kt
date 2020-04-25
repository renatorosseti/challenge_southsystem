package com.rosseti.southevents.main.events

sealed class EventsViewState {
    object ShowLoadingState: EventsViewState()
    data class ShowContentFeed(val events: List<String>): EventsViewState()
    data class ShowRequestError(val message: Int): EventsViewState()
}