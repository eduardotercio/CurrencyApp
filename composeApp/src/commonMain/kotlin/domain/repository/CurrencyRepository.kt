package domain.repository

import domain.model.ConversionCurrencies
import domain.model.Currency
import domain.model.DataResponse
import domain.model.RequestState

interface CurrencyRepository {

    suspend fun getLatestExchangeRates(getFromLocal: Boolean): RequestState<DataResponse>

    suspend fun saveLatestExchangeRates(vararg currencies: Currency)

    suspend fun getLastRequestTime(): Long

    suspend fun saveLastRequestTime(millisUpdated: Long)

    suspend fun getLastConversionCurrencies(currenciesList: List<Currency>): ConversionCurrencies?

    suspend fun saveLastConversionCurrencies(conversionCurrencies: ConversionCurrencies)
}