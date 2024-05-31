package com.nafi.airseat.presentation.otpaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class OtpViewModel(private val repository: UserRepository) : ViewModel() {
    fun doVerif(
        email: String,
        code: String,
    ) = repository
        .doVerif(
            email = email,
            code = code,
        )
        .asLiveData(Dispatchers.IO)

    fun doVerifResendOtp(email: String) =
        repository
            .doVerifResendOtp(email)
            .asLiveData(Dispatchers.IO)
}
