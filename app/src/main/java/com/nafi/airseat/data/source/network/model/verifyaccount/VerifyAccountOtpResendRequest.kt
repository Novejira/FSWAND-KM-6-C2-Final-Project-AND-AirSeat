package com.nafi.airseat.data.source.network.model.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifyAccountOtpResendRequest(
    @SerializedName("email")
    var email: String?,
)
