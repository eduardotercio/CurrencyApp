package data.service.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.model.CurrencyType
import domain.service.local.PreferencesService
import kotlinx.coroutines.flow.Flow

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
        if (currencyType is CurrencyType.SourceCurrency) {
            flowSettings.putString(
                key = SOURCE_CURRENCY_KEY,
                value = currencyType.source.name
            )
        } else if (currencyType is CurrencyType.TargetCurrency) {
            flowSettings.putString(
                key = TARGET_CURRENCY_KEY,
                value = currencyType.target.name
            )
        }
    }

    override suspend fun getLastSourceSelected(): Flow<String> {
        return flowSettings.getStringFlow(
            key = SOURCE_CURRENCY_KEY,
            defaultValue = DEFAULT_SOURCE_CURRENCY_CODE_VALUE
        )
    }

    override suspend fun getLastTargetSelected(): Flow<String> {
        return flowSettings.getStringFlow(
            key = TARGET_CURRENCY_KEY,
            defaultValue = DEFAULT_TARGET_CURRENCY_CODE_VALUE
        )
    }

    private companion object {
        const val TIMESTAMP_KEY = "lastUpdated"
        const val SOURCE_CURRENCY_KEY = "source"
        const val TARGET_CURRENCY_KEY = "target"

        const val DEFAULT_TIMESTAMP_VALUE = 0L
        const val DEFAULT_SOURCE_CURRENCY_CODE_VALUE = "BRL"
        const val DEFAULT_TARGET_CURRENCY_CODE_VALUE = "USD"
    }
}