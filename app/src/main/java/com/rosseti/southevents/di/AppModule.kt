package com.rosseti.southevents.di

import dagger.Module

@Module(includes = [
    SchedulerModule::class,
    NetworkModule::class,
    DialogModule::class])
class AppModule