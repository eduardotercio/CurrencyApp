package di

import com.russhwolf.settings.Settings
import data.mapper.ServiceResponseMapperImpl
import data.repository.CurrencyRepositoryImpl
import data.service.local.PreferencesServiceImpl
import data.service.remote.CurrencyApiServiceImpl
import domain.mapper.ServiceResponseMapper
import domain.repository.CurrencyRepository
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.HomeScreenViewModel
import kotlin.time.TimeSource

val commonModules = module {
    single { Settings() }

    factory<ServiceResponseMapper> {
        ServiceResponseMapperImpl()
    }

    single<CurrencyApiService> {
        CurrencyApiServiceImpl(
            client = get(),
            responseMapper = get()
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