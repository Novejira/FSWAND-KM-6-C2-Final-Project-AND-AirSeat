package com.nafi.airseat.di

import android.content.SharedPreferences
import com.nafi.airseat.core.BaseViewModel
import com.nafi.airseat.data.datasource.AirportHistoryDataSource
import com.nafi.airseat.data.datasource.AirportHistoryDataSourceImpl
import com.nafi.airseat.data.datasource.AuthDataSource
import com.nafi.airseat.data.datasource.AuthDataSourceImpl
import com.nafi.airseat.data.datasource.HistoryDataSource
import com.nafi.airseat.data.datasource.HistoryDataSourceImpl
import com.nafi.airseat.data.datasource.NotificationDataSource
import com.nafi.airseat.data.datasource.NotificationDataSourceImpl
import com.nafi.airseat.data.datasource.ProfileDataSource
import com.nafi.airseat.data.datasource.ProfileDataSourceImpl
import com.nafi.airseat.data.datasource.SearchHistoryDataSource
import com.nafi.airseat.data.datasource.SearchHistoryDataSourceImpl
import com.nafi.airseat.data.datasource.UserPrefDataSource
import com.nafi.airseat.data.datasource.UserPrefDataSourceImpl
import com.nafi.airseat.data.datasource.airport.AirportApiDataSource
import com.nafi.airseat.data.datasource.airport.AirportDataSource
import com.nafi.airseat.data.datasource.booking.BookingApiDataSource
import com.nafi.airseat.data.datasource.booking.BookingDataSource
import com.nafi.airseat.data.datasource.flight.FlightApiDataSource
import com.nafi.airseat.data.datasource.flight.FlightDataSource
import com.nafi.airseat.data.datasource.flightdetail.FlightDetailApiDataSource
import com.nafi.airseat.data.datasource.flightdetail.FlightDetailDataSource
import com.nafi.airseat.data.datasource.intro.IntroDataSource
import com.nafi.airseat.data.datasource.intro.IntroDataSourceImpl
import com.nafi.airseat.data.datasource.seat.SeatApiDataSource
import com.nafi.airseat.data.datasource.seat.SeatDataSource
import com.nafi.airseat.data.repository.AirportHistoryRepository
import com.nafi.airseat.data.repository.AirportHistoryRepositoryImpl
import com.nafi.airseat.data.repository.AirportRepository
import com.nafi.airseat.data.repository.AirportRepositoryImpl
import com.nafi.airseat.data.repository.BookingRepository
import com.nafi.airseat.data.repository.BookingRepositoryImpl
import com.nafi.airseat.data.repository.FlightDetailRepository
import com.nafi.airseat.data.repository.FlightDetailRepositoryImpl
import com.nafi.airseat.data.repository.FlightRepository
import com.nafi.airseat.data.repository.FlightRepositoryImpl
import com.nafi.airseat.data.repository.HistoryRepository
import com.nafi.airseat.data.repository.HistoryRepositoryImpl
import com.nafi.airseat.data.repository.IntroRepository
import com.nafi.airseat.data.repository.IntroRepositoryImpl
import com.nafi.airseat.data.repository.NotificationRepository
import com.nafi.airseat.data.repository.NotificationRepositoryImpl
import com.nafi.airseat.data.repository.ProfileRepository
import com.nafi.airseat.data.repository.ProfileRepositoryImpl
import com.nafi.airseat.data.repository.SearchHistoryRepository
import com.nafi.airseat.data.repository.SearchHistoryRepositoryImpl
import com.nafi.airseat.data.repository.SeatRepository
import com.nafi.airseat.data.repository.SeatRepositoryImpl
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.repository.UserPrefRepositoryImpl
import com.nafi.airseat.data.repository.UserRepository
import com.nafi.airseat.data.repository.UserRepositoryImpl
import com.nafi.airseat.data.source.local.database.AppDatabase
import com.nafi.airseat.data.source.local.database.dao.AirportHistoryDao
import com.nafi.airseat.data.source.local.database.dao.SearchHistoryDao
import com.nafi.airseat.data.source.local.pref.UserPreference
import com.nafi.airseat.data.source.local.pref.UserPreferenceImpl
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization
import com.nafi.airseat.data.source.network.services.TokenInterceptor
import com.nafi.airseat.presentation.appintro.AppIntroViewModel
import com.nafi.airseat.presentation.biodata.OrdererBioViewModel
import com.nafi.airseat.presentation.biodata.PassengerBioViewModel
import com.nafi.airseat.presentation.deleteaccount.DeleteAccountViewModel
import com.nafi.airseat.presentation.detailflight.DetailFlightViewModel
import com.nafi.airseat.presentation.detailhistory.DetailHistoryViewModel
import com.nafi.airseat.presentation.flightdetail.FlightDetailPriceViewModel
import com.nafi.airseat.presentation.history.HistoryViewModel
import com.nafi.airseat.presentation.home.HomeViewModel
import com.nafi.airseat.presentation.login.LoginViewModel
import com.nafi.airseat.presentation.notification.NotificationViewModel
import com.nafi.airseat.presentation.otpaccount.OtpViewModel
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordViewModel
import com.nafi.airseat.presentation.profile.ProfileViewModel
import com.nafi.airseat.presentation.register.RegisterViewModel
import com.nafi.airseat.presentation.resetpassword.ResetPasswordViewModel
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordViewModel
import com.nafi.airseat.presentation.resultsearch.ResultSearchViewModel
import com.nafi.airseat.presentation.resultsearchreturn.ResultSearchReturnViewModel
import com.nafi.airseat.presentation.searchticket.SearchTicketViewModel
import com.nafi.airseat.presentation.searcthistory.SearchHistoryViewModel
import com.nafi.airseat.presentation.seatbook.SeatViewModel
import com.nafi.airseat.presentation.seatbookreturn.SeatReturnViewModel
import com.nafi.airseat.presentation.splashscreen.SplashScreenViewModel
import com.nafi.airseat.presentation.updateprofile.UpdateProfileViewModel
import com.nafi.airseat.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<AirSeatApiService> { AirSeatApiService.invoke() }
            single { TokenInterceptor(get()) }
            single<AirSeatApiServiceWithAuthorization> { AirSeatApiServiceWithAuthorization.invoke(get()) }
        }

    private val serviceModule =
        module {
        }

    private val localModule =
        module {
            single<AppDatabase> { AppDatabase.getInstance(androidContext()) }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
            single<SearchHistoryDao> { get<AppDatabase>().searchHistoryDao() }
            single<AirportHistoryDao> { get<AppDatabase>().airportHistoryDao() }
        }

    private val datasource =
        module {
            single<SeatDataSource> { SeatApiDataSource(get()) }
            single<AuthDataSource> { AuthDataSourceImpl(get()) }
            single<UserPrefDataSource> { UserPrefDataSourceImpl(get()) }
            single<NotificationDataSource> { NotificationDataSourceImpl(get()) }
            single<BookingDataSource> { BookingApiDataSource(get()) }
            single<AirportDataSource> { AirportApiDataSource(get()) }
            single<FlightDataSource> { FlightApiDataSource(get()) }
            single<FlightDetailDataSource> { FlightDetailApiDataSource(get()) }
            single<ProfileDataSource> { ProfileDataSourceImpl(get()) }
            single<ProfileDataSource> { ProfileDataSourceImpl(get()) }
            single<HistoryDataSource> { HistoryDataSourceImpl(get()) }
            single<HistoryDataSource> { HistoryDataSourceImpl(get()) }
            single<IntroDataSource> { IntroDataSourceImpl(get()) }
            single<SearchHistoryDataSource> { SearchHistoryDataSourceImpl(get()) }
            single<AirportHistoryDataSource> { AirportHistoryDataSourceImpl(get()) }
            single<SeatDataSource> { SeatApiDataSource(get()) }
        }

    private val repository =
        module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<SeatRepository> { SeatRepositoryImpl(get()) }
            single<AirportRepository> { AirportRepositoryImpl(get()) }
            single<FlightRepository> { FlightRepositoryImpl(get()) }
            single<FlightDetailRepository> { FlightDetailRepositoryImpl(get()) }
            single<UserPrefRepository> { UserPrefRepositoryImpl(get()) }
            single<NotificationRepository> { NotificationRepositoryImpl(get()) }
            single<HistoryRepository> { HistoryRepositoryImpl(get()) }
            single<ProfileRepository> { ProfileRepositoryImpl(get()) }
            single<HistoryRepository> { HistoryRepositoryImpl(get()) }
            single<IntroRepository> { IntroRepositoryImpl(get()) }
            single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
            single<AirportHistoryRepository> { AirportHistoryRepositoryImpl(get()) }
            single<BookingRepository> { BookingRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::LoginViewModel)

            viewModelOf(::FlightDetailPriceViewModel)
            viewModel {
                SeatViewModel(get())
            }
            viewModel {
                SeatReturnViewModel(get())
            }
            viewModelOf(::AppIntroViewModel)
            viewModelOf(::SplashScreenViewModel)
            viewModelOf(::DeleteAccountViewModel)
            viewModel {
                RegisterViewModel(get())
            }
            viewModel {
                RegisterViewModel(get())
            }
            viewModel {
                OtpViewModel(get())
            }
            viewModel {
                OtpResetPasswordViewModel(get())
            }
            viewModel {
                ResetPasswordViewModel(get())
            }
            viewModel {
                ReqChangePasswordViewModel(get())
            }
            viewModel {
                SearchTicketViewModel(get(), get())
            }
            viewModel {
                ResultSearchViewModel(get())
            }
            viewModel {
                DetailFlightViewModel(get(), get())
            }
            viewModel {
                NotificationViewModel(get())
            }
            viewModel {
                BaseViewModel(get())
            }
            viewModel {
                HistoryViewModel(get())
            }
            viewModel {
                HistoryViewModel(get())
            }
            viewModel {
                ProfileViewModel(get(), get())
            }
            viewModel {
                SearchHistoryViewModel(get())
            }
            viewModel {
                UpdateProfileViewModel(get())
            }
            viewModel {
                DetailHistoryViewModel(get(), get())
            }
            viewModel {
                ResultSearchReturnViewModel(get())
            }
        }

    val modules =
        listOf<Module>(networkModule, serviceModule, localModule, datasource, repository, viewModelModule)
}
