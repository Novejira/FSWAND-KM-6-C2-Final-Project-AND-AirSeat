package com.nafi.airseat.data.network.user

import com.google.gson.annotations.SerializedName

data class CheckUserInformationResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: CheckUserInformationData,
)
