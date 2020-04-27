package com.rosseti.southevents.main.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rosseti.southevents.R
import com.rosseti.southevents.data.Cache.contentFeed
import com.rosseti.southevents.main.EventsRepository
import com.rosseti.southevents.main.model.*
import com.rosseti.southevents.util.NetworkUtil
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

    @MockK
    lateinit var networkUtil: NetworkUtil

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EventsViewModel(eventsRepository, networkUtil)
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
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.loadEvents() } returns Single.just(listResponse)

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(listResponse), response)
    }

    @Test(expected = NetworkException::class)
    fun fetchEvents_whenExceptionOccurs() {
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.loadEvents() } throws NetworkException(Throwable())

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return ShowRequestError with the error message.", EventsViewState.ShowRequestError(R.string.error_request), response)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailable() {
        contentFeed = listOf()
        every { networkUtil.isInternetAvailable() } returns false

        val response: EventsViewState = viewModel.fetchEvents()!!
        print("MESSAGE: $response")
        Assert.assertTrue("Should return ShowRequestError state view.", response is EventsViewState.ShowRequestError)

        response as EventsViewState.ShowRequestError

        Assert.assertTrue("Should return NoNetworkException.", response.networkException is NoNetworkException)
        Assert.assertEquals("Should return the no-network message error.", response.message, R.string.error_internet)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailableAndListEventsIsCached() {
        contentFeed = listOf(
            Event(
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
            )
        )

        every { networkUtil.isInternetAvailable() } returns false
        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(contentFeed), response)
    }
}