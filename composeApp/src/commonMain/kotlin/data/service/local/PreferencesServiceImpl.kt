package data.service.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.model.CurrencyType
import domain.service.local.PreferencesService

@OptIn(ExperimentalSettingsApi::class)
class PreferencesServiceImpl(
    settings: Settings
) : PreferencesService {

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()
    override suspend fun saveLastRequestTime(millisUpdated: Long) {
        flowSettings.putLong(
            key = TIMESTAMP_KEY,
            value = millisUpdated
        )
    }

    override suspend fun getLastRequestTime(): Long {
        return flowSettings.getLong(
            key = TIMESTAMP_KEY,
            defaultValue = DEFAULT_TIMESTAMP_VALUE
        )
    }

    override suspend fun saveSelectedCurrency(currencyType: CurrencyType) {
        currencyType.currencyCode?.let { currencyCode ->
            flowSettings.putString(
                key = currencyType.key,
                value = currencyCode.name
            )
        }
    }

    override suspend fun getLastSelectedCurrencyCode(currencyType: CurrencyType): String {
        return flowSettings.getString(
            key = currencyType.key,
            defaultValue = currencyType.defaultCode
        )
    }

    private companion object {
        const val TIMESTAMP_KEY = "lastUpdated"

        const val DEFAULT_TIMESTAMP_VALUE = 0L
    }
}