package data.service.remote

import data.model.ApiResponse
import domain.mapper.ServiceResponseMapper
import domain.model.DataResponse
import domain.model.RequestState
import domain.service.remote.CurrencyApiService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val client: HttpClient,
    private val responseMapper: ServiceResponseMapper
) : CurrencyApiService {

    override suspend fun getLatestExchangeRates(): RequestState<DataResponse> {
        return try {
            val request = client.get(ENDPOINT)
            if (request.status == HttpStatusCode.OK) {
                val apiResponse = Json.decodeFromString<ApiResponse>(request.body())

                RequestState.Success(data = responseMapper.mapApiToData(apiResponse))
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