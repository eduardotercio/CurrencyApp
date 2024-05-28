package domain.repository

import domain.model.DataResponse
import domain.model.RequestState

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(isDataFresh: Boolean): RequestState<DataResponse>

    suspend fun isDataFresh(currentTimestamp: Long): Boolean

    suspend fun saveTimestamp(millisUpdated: Long)

}