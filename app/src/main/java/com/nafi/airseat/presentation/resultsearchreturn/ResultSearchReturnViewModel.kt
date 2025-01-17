package com.nafi.airseat.presentation.resultsearchreturn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers

class ResultSearchReturnViewModel(private val repository: FlightRepository) : ViewModel() {
    fun getFlightData(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        destinationAirportId: String,
    ) = repository.getFlights(searchDateInput, sortByClass, orderBy, departureAirportId, destinationAirportId).asLiveData(
        Dispatchers.IO,
    )
}
