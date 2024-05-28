package domain.service.local

interface PreferencesService {

    suspend fun saveLastUpdated(millisUpdated: Long)
    suspend fun getLastUpdated(): Long
}