package com.rosseti.southevents.main.events

import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class EventsViewModelModule {
    @Provides
    fun providesEventsViewModel(
        repository: EventsRepository,
        networkUtil: NetworkUtil
    ) = EventsViewModel(repository, networkUtil)
}