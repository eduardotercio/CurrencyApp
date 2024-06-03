package domain.service.local

import domain.model.ConversionCurrencies
import domain.model.Currency

interface PreferencesService {

    suspend fun saveLastRequestTime(millisUpdated: Long)
    suspend fun getLastRequestTime(): Long

    suspend fun saveLastConversionCurrencies(conversionCurrencies: ConversionCurrencies)

    suspend fun getLastConversionCurrencies(currenciesList: List<Currency>): ConversionCurrencies?
}