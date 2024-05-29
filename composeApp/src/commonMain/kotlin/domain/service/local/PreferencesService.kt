package domain.service.local

interface PreferencesService {

    suspend fun saveLastRequestTime(millisUpdated: Long)
    suspend fun getLastRequestTime(): Long
}