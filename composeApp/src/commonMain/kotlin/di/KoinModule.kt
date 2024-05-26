package di

import com.russhwolf.settings.Settings
import data.local.PreferencesServiceImpl
import data.remote.CurrencyApiServiceImpl
import data.repository.CurrencyRepositoryImpl
import domain.local.PreferencesService
import domain.remote.CurrencyApiService
import domain.repository.CurrencyRepository
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
import kotlin.time.TimeSource

private const val TIME_OUT = 15000L
private const val API_KEY = "apikey"

val dataModule = module {
    single { Settings() }

    single<CurrencyApiService> {
        CurrencyApiServiceImpl(
            client = get()
        )
    }

    single<PreferencesService> {
        PreferencesServiceImpl(
            settings = get()
        )
    }

    single<CurrencyRepository> {
        CurrencyRepositoryImpl(
            currencyApi = get(),
            preferences = get(),
            currentTimestamp = TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
        )
    }
}

val domainModule = module {
    factory {
        HomeViewModel(
            service = get()
        )
    }
}

val ktorModule = module {
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
}

fun initializeKoin() {
    startKoin {
        modules(dataModule, domainModule, ktorModule)
    }
}