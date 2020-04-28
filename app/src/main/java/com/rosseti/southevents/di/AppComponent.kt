package com.rosseti.southevents.di

import com.rosseti.southevents.SouthEventsApp
import com.rosseti.southevents.main.details.EventDetailsViewModelModule
import com.rosseti.southevents.main.events.EventsViewModelModule
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    FragmentBuilder::class,
    EventsViewModelModule::class,
    EventDetailsViewModelModule::class])
interface AppComponent : AndroidInjector<SouthEventsApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SouthEventsApp>()
}