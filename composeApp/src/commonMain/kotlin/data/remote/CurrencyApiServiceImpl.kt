package data.remote

import domain.model.ApiResponse
import domain.model.CurrencyCode
import domain.model.RequestState
import domain.remote.CurrencyApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val client: HttpClient
) : CurrencyApiService {

    override suspend fun getLatestExchangeRates(): RequestState<ApiResponse> {
        return try {
            val request = client.get(ENDPOINT)
            if (request.status == HttpStatusCode.OK) {
                val apiResponse = Json.decodeFromString<ApiResponse>(request.body())

//                val availableCurrencyCodes = apiResponse.data.keys
//                    .filter {
//                        CurrencyCode.entries
//                            .map { code -> code.name }
//                            .toSet()
//                            .contains(it)
//                    }
//
//                val availableCurrencies = apiResponse.data.values
//                    .filter { currency ->
//                        availableCurrencyCodes.contains(currency.code)
//                    }

                RequestState.Success(data = apiResponse)
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