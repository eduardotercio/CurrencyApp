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
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.CurrentFormattedDateUseCaseImpl
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.LatestExchangeRatesUseCaseImpl
import domain.usecase.RatesStatusUseCase
import domain.usecase.RatesStatusUseCaseImpl
import domain.usecase.SaveCurrentMetaDataUseCase
import domain.usecase.SaveCurrentMetaDataUseCaseImpl
import org.koin.dsl.module

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
            preferences = get()
        )
    }

    factory<LatestExchangeRatesUseCase> {
        LatestExchangeRatesUseCaseImpl(
            repository = get()
        )
    }

    factory<CurrentFormattedDateUseCase> {
        CurrentFormattedDateUseCaseImpl()
    }

    factory<RatesStatusUseCase> {
        RatesStatusUseCaseImpl(
            repository = get()
        )
    }

    factory<SaveCurrentMetaDataUseCase> {
        SaveCurrentMetaDataUseCaseImpl(
            repository = get()
        )
    }
}