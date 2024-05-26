package domain.repository

import domain.model.DataResponse
import domain.model.RequestState

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(): RequestState<DataResponse>

    suspend fun saveTimestamp(timestampUpdated: String)

}