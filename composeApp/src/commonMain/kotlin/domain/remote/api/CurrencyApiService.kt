package domain.remote.api

import domain.model.Currency
import domain.model.RequestState

interface CurrencyApiService {

    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}