package com.rosseti.southevents.main.details

import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class EventDetailsViewModelModule {
    @Provides
    fun providesDetailsViewModel(
        repository: EventsRepository,
        networkUtil: NetworkUtil
    ) = EventDetailsViewModel(repository, networkUtil)
}