package com.rosseti.southevents

import com.rosseti.southevents.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SouthEventsApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out SouthEventsApp> = DaggerAppComponent.builder().create(this)
}