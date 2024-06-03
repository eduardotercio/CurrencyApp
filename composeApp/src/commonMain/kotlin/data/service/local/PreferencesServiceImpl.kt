package data.service.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.model.ConversionCurrencies
import domain.model.Currency
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

    override suspend fun saveLastConversionCurrencies(conversionCurrencies: ConversionCurrencies) {
        saveCurrencyCode(conversionCurrencies.source)
        saveCurrencyCode(conversionCurrencies.target)
    }

    private suspend fun saveCurrencyCode(currency: Currency) {
        flowSettings.putString(
            key = CURRENCY_KEY,
            value = currency.code
        )
    }

    override suspend fun getLastConversionCurrencies(currenciesList: List<Currency>): ConversionCurrencies? {
        val sourceCode = getCurrencyCode()
        val targetCode = getCurrencyCode()

        val source = currenciesList.firstOrNull { it.code == sourceCode }
        val target = currenciesList.firstOrNull { it.code == targetCode }

        return if (source == null || target == null) null
        else ConversionCurrencies(source, target)
    }

    private suspend fun getCurrencyCode(): String {
        return flowSettings.getString(
            key = CURRENCY_KEY,
            defaultValue = DEFAULT_CURRENCY_CODE_VALUE
        )
    }

    private companion object {
        const val TIMESTAMP_KEY = "lastUpdated"
        const val CURRENCY_KEY = "code"

        const val DEFAULT_TIMESTAMP_VALUE = 0L
        const val DEFAULT_CURRENCY_CODE_VALUE = "BRL"
    }
}