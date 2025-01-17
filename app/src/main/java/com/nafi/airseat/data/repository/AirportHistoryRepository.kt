package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.AirportHistoryDataSource
import com.nafi.airseat.data.mapper.toAirportHistoryEntity
import com.nafi.airseat.data.mapper.toAirportHistoryList
import com.nafi.airseat.data.model.AirportHistory
import com.nafi.airseat.data.source.local.database.entity.AirportHistoryEntity
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceed
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface AirportHistoryRepository {
    fun getAllAirportHistory(): Flow<ResultWrapper<List<AirportHistory>>>

    fun createAirportHistory(airportHistory: AirportHistory): Flow<ResultWrapper<Boolean>>

    fun deleteAirportHistory(airportHistory: AirportHistory): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class AirportHistoryRepositoryImpl(private val dataSource: AirportHistoryDataSource) : AirportHistoryRepository {
    override fun getAllAirportHistory(): Flow<ResultWrapper<List<AirportHistory>>> {
        return dataSource.getAllAirportHistory()
            .map {
                proceed {
                    it.toAirportHistoryList()
                }
            }.map {
                if (it.payload?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
            }
    }

    override fun createAirportHistory(airportHistory: AirportHistory): Flow<ResultWrapper<Boolean>> {
        return airportHistory.id.let { id ->
            proceedFlow {
                val affectedRow =
                    dataSource.addAirportHistory(
                        AirportHistoryEntity(
                            id = id,
                            airportHistory = airportHistory.airportHistory,
                        ),
                    )
                affectedRow > 0
            }
        }
    }

    override fun deleteAirportHistory(airportHistory: AirportHistory): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteAirportHistory(airportHistory.toAirportHistoryEntity()) == 0 }
    }

    override fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            dataSource.deleteAll()
            true
        }
    }
}
