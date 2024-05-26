package domain.remote

import domain.model.ApiResponse
import domain.model.RequestState

interface CurrencyApiService {

    suspend fun getLatestExchangeRates(): RequestState<ApiResponse>
}