package data.remote.api

import domain.model.ApiResponse
import domain.model.Currency
import domain.model.RequestState
import domain.remote.api.CurrencyApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val client: HttpClient
) : CurrencyApiService {

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val request = client.get(ENDPOINT)
            if (request.status == HttpStatusCode.OK) {
                val apiResponse = Json.decodeFromString<ApiResponse>(request.body())
                RequestState.Success(data = apiResponse.data.values.toList())
            } else {
                RequestState.Error(message = "Error: ${request.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
    }
}