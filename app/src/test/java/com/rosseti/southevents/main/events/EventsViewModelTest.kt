package com.rosseti.southevents.main.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rosseti.southevents.main.EventsRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class EventsViewModelTest {
    private lateinit var viewModel: EventsViewModel

    @MockK
    lateinit var eventsRepository: EventsRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EventsViewModel(eventsRepository)
    }

    @Test
    fun fetchEvents_whenSucceed() {
        val listResponse: List<String> = listOf("Mock item test.")
        every { eventsRepository.loadEvents() } returns Single.just(listResponse)

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(listResponse), response)

    }
}