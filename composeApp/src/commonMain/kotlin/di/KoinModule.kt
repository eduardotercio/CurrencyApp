package di

import data.remote.api.CurrencyApiServiceImpl
import domain.remote.api.CurrencyApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.currencyapptest.BuildKonfig
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.HomeViewModel

private const val TIME_OUT = 15000L
private const val API_KEY = "apikey"

val appModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TIME_OUT
            }
            install(DefaultRequest) {
                headers {
                    append(API_KEY, BuildKonfig.CURRENCY_API_KEY)
                }
            }
        }
    }

    single<CurrencyApiService> {
        CurrencyApiServiceImpl(
            client = get()
        )
    }
    factory {
        HomeViewModel(
            service = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}