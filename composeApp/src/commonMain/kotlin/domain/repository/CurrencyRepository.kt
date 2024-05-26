package domain.repository

import domain.model.ApiResponse
import domain.model.RequestState

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(): RequestState<ApiResponse>

    suspend fun saveTimestamp(timestampUpdated: String)

}