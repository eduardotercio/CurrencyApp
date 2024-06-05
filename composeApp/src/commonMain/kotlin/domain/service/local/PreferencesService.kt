package domain.service.local

import domain.model.CurrencyType
import kotlinx.coroutines.flow.Flow

interface PreferencesService {

    suspend fun saveLastRequestTime(millisUpdated: Long)
    suspend fun getLastRequestTime(): Long

    suspend fun saveSelectedCurrency(currencyType: CurrencyType)

    suspend fun getLastSourceSelected(): Flow<String>

    suspend fun getLastTargetSelected(): Flow<String>
}