package data.remote.api

import domain.remote.api.CurrencyApiService
import io.ktor.client.HttpClient

class CurrencyApiServiceImpl(
    private val client: HttpClient
) : CurrencyApiService {

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
    }
}