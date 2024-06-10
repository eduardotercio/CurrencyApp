package domain.service.local

import domain.model.CurrencyType

interface PreferencesService {

    suspend fun saveLastRequestTime(millisUpdated: Long)
    suspend fun getLastRequestTime(): Long

    suspend fun saveSelectedCurrency(currencyType: CurrencyType)

    suspend fun getLastSelectedCurrencyCode(currencyType: CurrencyType): String
}