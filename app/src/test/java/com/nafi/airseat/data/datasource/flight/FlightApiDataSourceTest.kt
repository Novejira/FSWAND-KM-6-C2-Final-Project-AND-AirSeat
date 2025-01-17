package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FlightApiDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiService

    private lateinit var ds: FlightDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ds = FlightApiDataSource(service)
    }

    @Test
    fun getFlightList() =
        runTest {
            val mockResponse = mockk<FlightsResponse>(relaxed = true)
            coEvery { service.getFlights(any(), any(), any(), any(), any(), any(), any()) } returns mockResponse
            runBlocking {
                val actualResponse = ds.getFlightList("08-01-2024", "price_economy", "asc", "1", "2")
                coVerify { service.getFlights("08-01-2024", "price_economy", 20, 1, "asc", "1", "2") }
                assertEquals(actualResponse, mockResponse)
            }
        }
}
