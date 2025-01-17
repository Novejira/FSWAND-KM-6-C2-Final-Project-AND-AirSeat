package com.nafi.airseat.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val favoriteDestinationRepository: FlightRepository,
) : ViewModel() {
    fun getFlightData(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        destinationAirportId: String,
    ) = favoriteDestinationRepository.getFlights(
        searchDateInput,
        sortByClass,
        orderBy,
        departureAirportId,
        destinationAirportId,
    ).asLiveData(
        Dispatchers.IO,
    )
}
