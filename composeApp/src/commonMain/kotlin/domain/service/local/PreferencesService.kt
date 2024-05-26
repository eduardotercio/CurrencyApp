package domain.service.local

interface PreferencesService {

    suspend fun saveLastUpdated(lastUpdated: String)
    suspend fun getLastUpdated(): Long
}