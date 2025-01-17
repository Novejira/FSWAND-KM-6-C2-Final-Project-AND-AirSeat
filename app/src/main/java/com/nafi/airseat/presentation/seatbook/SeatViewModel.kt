package com.nafi.airseat.presentation.seatbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.SeatRepository
import kotlinx.coroutines.Dispatchers

class SeatViewModel(private val repository: SeatRepository) : ViewModel() {
    fun getFormattedSeatData(
        flightId: String,
        seatClassChoose: String,
    ) = repository.getFormattedSeatData(
        flightId,
        seatClassChoose,
    ).asLiveData(Dispatchers.IO)
}
