package com.nafi.airseat.data.source.network.model.user

import com.google.gson.annotations.SerializedName

data class CheckUserInformationData(
    @SerializedName("user")
    var user: CheckUserInformationDataUser,
)
