package com.rosseti.southevents.di

import com.rosseti.southevents.main.checkin.CheckInFragment
import com.rosseti.southevents.main.details.EventDetailsFragment
import com.rosseti.southevents.main.events.EventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeEventListFragment(): EventsFragment

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeEventDetailsFragment(): EventDetailsFragment

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeCheckInFragment(): CheckInFragment
}