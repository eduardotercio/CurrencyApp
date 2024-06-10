package data.repository

import domain.model.Currency
import domain.model.CurrencyType
import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository
import domain.service.local.MongoDBService
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApiService,
    private val preferences: PreferencesService,
    private val mongoDb: MongoDBService
) : CurrencyRepository {

    override suspend fun getLatestExchangeRates(getFromLocal: Boolean): RequestState<DataResponse> {
        return if (getFromLocal) {
            getExchangeRatesFromLocal().takeIf { it.isSuccess() } ?: getExchangeRatesFromApi()
        } else {
            getExchangeRatesFromApi()
        }
    }

    private suspend fun getExchangeRatesFromApi(): RequestState<DataResponse> =
        withContext(Dispatchers.IO) {
            println("Repository: ExchangeRatesFromApi")
            currencyApi.getLatestExchangeRates()
        }

    private suspend fun getExchangeRatesFromLocal(): RequestState<DataResponse> =
        withContext(Dispatchers.IO) {
            println("Repository: ExchangeRatesFromLocal")
            mongoDb.readCurrencies().first()
        }

    override suspend fun saveLatestExchangeRates(vararg currencies: Currency) =
        withContext(Dispatchers.IO) {
            mongoDb.insertCurrencies(*currencies)
        }

    override suspend fun getLastRequestTime(): Long = withContext(Dispatchers.IO) {
        preferences.getLastRequestTime()
    }

    override suspend fun saveLastRequestTime(millisUpdated: Long) = withContext(Dispatchers.IO) {
        preferences.saveLastRequestTime(millisUpdated)
    }

    override suspend fun getSavedSelectedCurrencyCode(currencyType: CurrencyType): String =
        withContext(Dispatchers.IO) {
            preferences.getLastSelectedCurrencyCode(currencyType)
        }

    override suspend fun saveSelectedCurrency(currencyType: CurrencyType) =
        withContext(Dispatchers.IO) {
            preferences.saveSelectedCurrency(currencyType)
        }
}