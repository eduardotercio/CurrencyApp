package data.repository

import domain.model.ConversionCurrencies
import domain.model.Currency
import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository
import domain.service.local.MongoDBService
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService
import kotlinx.coroutines.flow.first


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

    private suspend fun getExchangeRatesFromApi(): RequestState<DataResponse> {
        println("Repository: ExchangeRatesFromApi")
        return currencyApi.getLatestExchangeRates()
    }

    private suspend fun getExchangeRatesFromLocal(): RequestState<DataResponse> {
        println("Repository: ExchangeRatesFromLocal")
        return mongoDb.readCurrencies().first()
    }

    override suspend fun saveLatestExchangeRates(vararg currencies: Currency) {
        mongoDb.insertCurrencies(*currencies)
    }

    override suspend fun getLastRequestTime(): Long {
        return preferences.getLastRequestTime()
    }

    override suspend fun saveLastRequestTime(millisUpdated: Long) =
        preferences.saveLastRequestTime(millisUpdated)

    override suspend fun getLastConversionCurrencies(): ConversionCurrencies {
        return ConversionCurrencies()
    }

    override suspend fun saveLastConversionCurrencies() {

    }

}