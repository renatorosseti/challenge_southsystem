package com.rosseti.southevents.main.events

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosseti.southevents.R
import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.main.model.HttpCallFailureException
import com.rosseti.southevents.main.model.NoNetworkException
import com.rosseti.southevents.main.model.ServerUnreachableException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EventsViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    var eventsResponse = MutableLiveData<EventsViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = EventsViewModel::class.simpleName

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchEvents(): EventsViewState? {
        eventsResponse.value = EventsViewState.ShowLoadingState

        val eventsDisposable: Disposable = eventsRepository.loadEvents()
            .subscribe(
                {
                    eventsResponse.value = EventsViewState.ShowContentFeed(it)
                },
                {
                    eventsResponse.value = EventsViewState.ShowRequestError(R.string.error_request)
                    handleError(error = it)
                }
            )
        disposable.add(eventsDisposable)

        return eventsResponse.value
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is NoNetworkException -> Log.e(TAG,"Internet not available. ${error.message}")
            is ServerUnreachableException -> Log.e(TAG,"Server is unreachable. ${error.message}")
            is HttpCallFailureException -> Log.e(TAG,"Call failed. ${error.message}")
            else -> Log.e(TAG,"Error: ${error.message}.")
        }
    }


}