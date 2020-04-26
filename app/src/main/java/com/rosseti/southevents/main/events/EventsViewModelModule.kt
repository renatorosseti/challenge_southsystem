package com.rosseti.southevents.main.events

import com.rosseti.southevents.main.EventsRepository
import dagger.Module
import dagger.Provides

@Module
class EventsViewModelModule {

    @Provides
    fun providesEventsViewModel(repository: EventsRepository) = EventsViewModel(repository)
}