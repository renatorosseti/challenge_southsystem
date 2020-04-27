package com.rosseti.southevents.main.events

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rosseti.southevents.R
import com.rosseti.southevents.data.Cache.contentFeed
import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.main.model.HttpCallFailureException
import com.rosseti.southevents.main.model.NoNetworkException
import com.rosseti.southevents.main.model.ServerUnreachableException
import com.rosseti.southevents.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EventsViewModel(
    private val eventsRepository: EventsRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    var response = MediatorLiveData<EventsViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = EventsViewModel::class.simpleName

    init {
        response.addSource(networkUtil, Observer {
            if (!networkUtil.isInternetAvailable()) {
                response.value = EventsViewState.ShowRequestError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            } else {
                if (contentFeed.isNotEmpty()) {
                    response.value = EventsViewState.ShowContentFeed(contentFeed)
                } else {
                    fetchEvents()
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchEvents(): EventsViewState? {
        response.value = EventsViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            val eventsDisposable: Disposable = eventsRepository.loadEvents()
                .subscribe(
                    {
                        contentFeed = it
                        response.value = EventsViewState.ShowContentFeed(it)
                    },
                    {
                        handleError(error = it)
                    }
                )
            disposable.add(eventsDisposable)
        } else {
            if (contentFeed.isNotEmpty()) {
                response.value = EventsViewState.ShowContentFeed(contentFeed)
            } else {
                response.value = EventsViewState.ShowRequestError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            }
        }

        return response.value
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                response.value = EventsViewState.ShowRequestError(R.string.error_internet, error)
                Log.e(TAG,"Internet not available. ${error.message}")
            }
            is ServerUnreachableException -> {
                response.value = EventsViewState.ShowRequestError(R.string.error_request, error)
                Log.e(TAG,"Server is unreachable. ${error.message}")
            }
            is HttpCallFailureException -> {
                response.value = EventsViewState.ShowRequestError(R.string.error_request, error)
                Log.e(TAG,"Call failed. ${error.message}")
            }
            else -> {
                response.value = EventsViewState.ShowRequestError(R.string.error_request)
                Log.e(TAG,"Error: ${error.message}.")
            }
        }
    }

}