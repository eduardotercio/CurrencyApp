package domain.repository

import domain.model.ConversionCurrencies
import domain.model.DataResponse
import domain.model.RequestState

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(getFromLocal: Boolean): RequestState<DataResponse>

    suspend fun getLastRequestTime(): Long

    suspend fun saveLastRequestTime(millisUpdated: Long)

    suspend fun getLastConversionCurrencies(): ConversionCurrencies

    suspend fun saveLastConversionCurrencies()

}