package domain.service.remote

import domain.model.DataResponse
import domain.model.RequestState

interface CurrencyApiService {

    suspend fun getLatestExchangeRates(): RequestState<DataResponse>
}