package com.rosseti.southevents.di

import com.rosseti.southevents.SouthEventsApp
import com.rosseti.southevents.api.Api
import com.rosseti.southevents.dialog.ProgressDialog
import com.rosseti.southevents.util.NetworkUtil
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
    @Provides
    fun providesRetrofit(): Api = Retrofit.Builder()
            .baseUrl(Api.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api::class.java)

    @Provides
    fun providesNetworkUtil(application: SouthEventsApp) = NetworkUtil(application)

    @Provides
    fun providesProgressDialog() = ProgressDialog()
}