package com.rosseti.southevents.main

import com.rosseti.southevents.api.Api
import com.rosseti.southevents.main.model.*
import io.reactivex.Single
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val api: Api,
    private val rxSchedulers: AppRxSchedulers
) {

    fun loadEvents(): Single<List<Event>> {
        return api.fetchEvents()
            .compose(mapNetworkErrors())
            .subscribeOn(rxSchedulers.network)
            .observeOn(rxSchedulers.main)
    }

    private fun <R> mapNetworkErrors() = {
            single: Single<R> ->
        single.onErrorResumeNext {
                error -> when (error) {
            is SocketTimeoutException -> Single.error(NoNetworkException(error))
            is UnknownHostException -> Single.error(ServerUnreachableException(error))
            is HttpException -> Single.error(HttpCallFailureException(error))
            else -> Single.error(NoNetworkException(error))
        }
        }
    }
}