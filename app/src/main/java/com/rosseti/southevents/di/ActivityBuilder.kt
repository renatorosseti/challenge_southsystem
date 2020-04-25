package com.rosseti.southevents.di

import com.rosseti.southevents.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeMainActivity(): MainActivity
}