package data.repository

import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService


class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApiService,
    private val preferences: PreferencesService,
) : CurrencyRepository {

    override suspend fun getLatestExchangeRates(getFromLocal: Boolean): RequestState<DataResponse> {
        return if (getFromLocal) {
            TODO()
        } else {
            currencyApi.getLatestExchangeRates()
        }
    }

    override suspend fun getLastRequestTime(): Long {
        return preferences.getLastRequestTime()
    }

    override suspend fun saveLastRequestTime(millisUpdated: Long) =
        preferences.saveLastRequestTime(millisUpdated)

}