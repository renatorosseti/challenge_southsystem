package com.rosseti.southevents.main.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rosseti.southevents.R
import com.rosseti.southevents.data.Cache.contendDetail
import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.main.model.*
import com.rosseti.southevents.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EventDetailsViewModel(
    private val eventsRepository: EventsRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    var response = MediatorLiveData<EventDetailsViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = EventDetailsViewModel::class.simpleName

    init {
        response.addSource(networkUtil, Observer {
            if (!networkUtil.isInternetAvailable()) {
                response.value = EventDetailsViewState.ShowNetworkError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            } else {
                response.value = EventDetailsViewState.RefreshEventDetails
            }
        })
    }

    fun handleBundleData(arguments: Bundle?): EventDetailsViewState? {
        response.value = EventDetailsViewState.RefreshEventDetails
        fun argString(key: String) = arguments?.getString(key)

        val hasEventChecked: Boolean? = arguments?.getBoolean("checkIn")
        if (hasEventChecked != null && hasEventChecked) {
            val name = argString("name")
            val email = argString("email")
            requestCheckIn(eventId = contendDetail.id, name = name!!, email = email!!)
        }

        return response.value
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun navigateToLocationEvent(context: Context, event: Event): EventDetailsViewState? {
        val gmmIntentUri: Uri = Uri.parse("geo:${event.latitude},${event.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context?.packageManager) != null) {
            context.startActivity(mapIntent)
        }
        response.value = EventDetailsViewState.RefreshEventDetails
        return response.value
    }

    fun navigateToSharingEvent(context: Context, event: Event): EventDetailsViewState? {
        response.value = EventDetailsViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val message =
                "${event.title}\n\n${event.description}\n\nR$${event.price}\n\n${event.image}"
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(shareIntent, "Share event using"))
            response.value = EventDetailsViewState.RefreshEventDetails
        } else {
            response.value = EventDetailsViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
        }

        return response.value
    }

    fun requestCheckIn(eventId: String, name: String, email: String): EventDetailsViewState? {
        response.value = EventDetailsViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            var checkInRequest = CheckInRequest(eventId = eventId, name = name, email = email)
            val eventsDisposable: Disposable = eventsRepository.requestEventCheckIn(checkInRequest)
                .subscribe(
                    {
                        response.value = EventDetailsViewState.ShowCheckInSucceed
                    },
                    {
                        handleError(error = it)
                    }
                )
            disposable.add(eventsDisposable)
        } else {
            response.value = EventDetailsViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
        }

        return response.value
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                response.value =
                    EventDetailsViewState.ShowNetworkError(R.string.error_internet, error)
                Log.e(TAG, "Internet not available. ${error.message}")
            }
            is ServerUnreachableException -> {
                response.value =
                    EventDetailsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Server is unreachable. ${error.message}")
            }
            is HttpCallFailureException -> {
                response.value =
                    EventDetailsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Call failed. ${error.message}")
            }
            else -> {
                response.value = EventDetailsViewState.ShowNetworkError(R.string.error_request)
                Log.e(TAG, "Error: ${error.message}.")
            }
        }
    }
}