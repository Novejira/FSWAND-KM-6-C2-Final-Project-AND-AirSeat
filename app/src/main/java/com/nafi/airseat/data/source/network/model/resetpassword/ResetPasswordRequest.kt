package com.nafi.airseat.data.source.network.model.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email")
    var email: String?,
)
