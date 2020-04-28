package com.rosseti.southevents.di

import com.rosseti.southevents.dialog.MessageDialog
import com.rosseti.southevents.dialog.ProgressDialog
import dagger.Module
import dagger.Provides

@Module
class DialogModule {
    @Provides
    fun providesErrorDialog(): MessageDialog = MessageDialog()

    @Provides
    fun providesProgressDialog(): ProgressDialog = ProgressDialog()
}