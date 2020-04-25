package com.rosseti.southevents.main.events

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosseti.southevents.main.EventsRepository
import io.reactivex.disposables.Disposable

class EventsViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    var eventsResponse = MutableLiveData<EventsViewState>()

    private val TAG = EventsViewModel::class.simpleName

    fun fetchEvents(): EventsViewState? {
        eventsResponse.value = EventsViewState.ShowLoadingState

        val eventsDisposable: Disposable = eventsRepository.loadEvents()
            .subscribe(
                {
                    eventsResponse.value = EventsViewState.ShowContentFeed(it)
                },
                {
                    Log.e(TAG, "Server error: ${it.message}")
                }
            )

        return eventsResponse.value
    }


}