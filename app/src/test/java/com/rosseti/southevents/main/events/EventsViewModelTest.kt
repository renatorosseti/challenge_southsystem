package com.rosseti.southevents.main.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rosseti.southevents.R
import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.main.model.Coupon
import com.rosseti.southevents.main.model.Event
import com.rosseti.southevents.main.model.NetworkException
import com.rosseti.southevents.main.model.Person
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
        val listResponse: List<Event> = listOf(Event(
            people = listOf(Person()),
            date = 1.0,
            description = "description",
            image = "image",
            longitude = 1.0,
            latitude = 1.0,
            price = 1.0,
            title = "Event - Test",
            id = "id",
            coupons = listOf(Coupon())
        ))
        every { eventsRepository.loadEvents() } returns Single.just(listResponse)

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(listResponse), response)
    }

    @Test(expected = NetworkException::class)
    fun fetchEvents_whenExceptionOccurs() {
        val listResponse: List<Event> = listOf(Event(
            people = listOf(Person()),
            date = 1.0,
            description = "description",
            image = "image",
            longitude = 1.0,
            latitude = 1.0,
            price = 1.0,
            title = "Event - Test",
            id = "id",
            coupons = listOf(Coupon())
        ))
        every { eventsRepository.loadEvents() } throws NetworkException(Throwable())

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowRequestError(R.string.error_request), response)
    }
}