package com.nafi.airseat.data.source.network.services

import com.nafi.airseat.BuildConfig
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.source.network.model.airport.AirportResponse
import com.nafi.airseat.data.source.network.model.booking.BookingData
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest
import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailResponse
import com.nafi.airseat.data.source.network.model.history.HistoryData
import com.nafi.airseat.data.source.network.model.login.LoginRequest
import com.nafi.airseat.data.source.network.model.notification.NotificationList
import com.nafi.airseat.data.source.network.model.profile.ProfileData
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.data.source.network.model.register.RegisterData
import com.nafi.airseat.data.source.network.model.register.RegisterRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.model.seat.SeatResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendData
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AirSeatApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): BaseResponse<Any>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): BaseResponse<RegisterData>

    @POST("auth/activation/verify")
    suspend fun verifyAccountOtp(
        @Body verifyAccountOtpRequest: VerifyAccountOtpRequest,
    ): BaseResponse<Any>

    @POST("auth/activation/resend")
    suspend fun verifyAccountOtpResend(
        @Body verifyAccountOtpRequest: VerifyAccountOtpResendRequest,
    ): BaseResponse<VerifyAccountOtpResendData>

    @POST("auth/password-reset/resend")
    suspend fun resetPasswordResendOtp(
        @Body resetPasswordResendOtpRequest: ResetPasswordResendOtpRequest,
    ): BaseResponse<ResetPasswordResendOtpData>

    @POST("auth/password-reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest,
    ): BaseResponse<ResetPasswordData>

    @POST("auth/password-reset/verify")
    suspend fun verifyPasswordChangeOtp(
        @Body verifyPasswordChangeOtpRequest: VerifyPasswordChangeOtpRequest,
    ): BaseResponse<Any>

    @GET("airport")
    suspend fun getAirportsByQuery(
        @Query("cityName") cityName: String?,
    ): AirportResponse

    @GET("flight")
    suspend fun getFlights(
        @Query("searchDate") searchDate: String?,
        @Query("sortBy") sortBy: String?,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
        @Query("order") order: String?,
        @Query("deptAirport") deptAirport: String?,
        @Query("arrAirport") arrAirport: String?,
    ): FlightsResponse

    @GET("flight/{id}")
    suspend fun getFlightsDetail(
        @Path("id") id: String,
    ): FlightDetailResponse

    companion object {
        @JvmStatic
        operator fun invoke(): AirSeatApiService {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(AirSeatApiService::class.java)
        }
    }
}

interface AirSeatApiServiceWithAuthorization {
    @GET("notification")
    suspend fun getNotification(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
    ): BaseResponse<NotificationList>

    @GET("booking/detail")
    suspend fun getHistoryData(
        @Query("limit") limit: Int = 99,
        @Query("page") page: Int = 1,
        @Query("bookingCode") bookingCode: String?,
        @Query("sortBy") sortBy: String = "transactionDate",
        @Query("order") order: String = "desc",
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
    ): BaseResponse<HistoryData>

    @GET("auth/me")
    suspend fun getUserProfile(): BaseResponse<ProfileData>

    @PATCH("profile")
    suspend fun updateProfile(
        @Body updateProfileData: UpdateProfileRequest,
    ): BaseResponse<Any>

    @DELETE("profile")
    suspend fun deleteAccount(): BaseResponse<Any>

    @GET("seat/flight/{id}")
    suspend fun getSeatData(
        @Path("id") flightId: String,
        @Query("seatClass") seatClass: String,
    ): SeatResponse

    @POST("booking")
    suspend fun bookingFlight(
        @Body bookingFlightRequest: BookingFlightRequest,
    ): BaseResponse<BookingData>

    companion object {
        @JvmStatic
        operator fun invoke(userPrefRepository: UserPrefRepository): AirSeatApiServiceWithAuthorization {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(TokenInterceptor(userPrefRepository))
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(AirSeatApiServiceWithAuthorization::class.java)
        }
    }
}
