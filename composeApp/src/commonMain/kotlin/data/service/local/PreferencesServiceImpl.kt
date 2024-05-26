package data.service.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.service.local.PreferencesService
import kotlinx.datetime.Instant

@OptIn(ExperimentalSettingsApi::class)
class PreferencesServiceImpl(
    private val settings: Settings
) : PreferencesService {

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()
    override suspend fun saveLastUpdated(lastUpdated: String) {
        flowSettings.putLong(
            key = TIMESTAMP_KEY,
            value = Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    override suspend fun getLastUpdated(): Long {
        return flowSettings.getLong(
            key = TIMESTAMP_KEY,
            defaultValue = DEFAULT_TIMESTAMP_VALUE
        )
    }

    private companion object {
        const val TIMESTAMP_KEY = "lastUpdated"
        const val DEFAULT_TIMESTAMP_VALUE = 0L
    }
}