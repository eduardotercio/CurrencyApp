package domain.repository

import domain.model.Currency
import domain.model.CurrencyType
import domain.model.DataResponse
import domain.model.RequestState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(getFromLocal: Boolean): RequestState<DataResponse>

    suspend fun saveLatestExchangeRates(vararg currencies: Currency)

    suspend fun getLastRequestTime(): Long

    suspend fun saveLastRequestTime(millisUpdated: Long)

    suspend fun getSavedSourceCurrencyCode(): Flow<String>

    suspend fun getSavedTargetCurrencyCode(): Flow<String>

    suspend fun saveSelectedCurrency(currencyType: CurrencyType)
}