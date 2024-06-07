package di

import com.russhwolf.settings.Settings
import data.mapper.ServiceResponseMapperImpl
import data.repository.CurrencyRepositoryImpl
import data.service.local.MongoDBServiceImpl
import data.service.local.PreferencesServiceImpl
import data.service.remote.CurrencyApiServiceImpl
import domain.mapper.ServiceResponseMapper
import domain.repository.CurrencyRepository
import domain.service.local.MongoDBService
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService
import domain.usecase.ConvertCurrenciesUseCase
import domain.usecase.ConvertCurrenciesUseCaseImpl
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.CurrentFormattedDateUseCaseImpl
import domain.usecase.GetSavedSourceCurrencyCodeUseCase
import domain.usecase.GetSavedSourceCurrencyCodeUseCaseImpl
import domain.usecase.GetSavedTargetCurrencyCodeUseCase
import domain.usecase.GetSavedTargetCurrencyCodeUseCaseImpl
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.LatestExchangeRatesUseCaseImpl
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.SaveLastRequestTimeUseCaseImpl
import domain.usecase.SaveLatestExchangeRatesUseCase
import domain.usecase.SaveLatestExchangeRatesUseCaseImpl
import domain.usecase.SaveSelectedCurrencyUseCase
import domain.usecase.SaveSelectedCurrencyUseCaseImpl
import domain.usecase.TimeFromLastRequestUseCase
import domain.usecase.TimeFromLastRequestUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModules = module {
    // Data
    singleOf(::Settings)
    factoryOf(::ServiceResponseMapperImpl) bind ServiceResponseMapper::class
    singleOf(::MongoDBServiceImpl) bind MongoDBService::class
    singleOf(::CurrencyApiServiceImpl) bind CurrencyApiService::class
    singleOf(::PreferencesServiceImpl) bind PreferencesService::class
    singleOf(::CurrencyRepositoryImpl) bind CurrencyRepository::class

    // Domain
    factoryOf(::CurrentFormattedDateUseCaseImpl) bind CurrentFormattedDateUseCase::class
    factoryOf(::LatestExchangeRatesUseCaseImpl) bind LatestExchangeRatesUseCase::class
    factoryOf(::SaveLatestExchangeRatesUseCaseImpl) bind SaveLatestExchangeRatesUseCase::class
    factoryOf(::TimeFromLastRequestUseCaseImpl) bind TimeFromLastRequestUseCase::class
    factoryOf(::SaveLastRequestTimeUseCaseImpl) bind SaveLastRequestTimeUseCase::class
    factoryOf(::GetSavedSourceCurrencyCodeUseCaseImpl) bind GetSavedSourceCurrencyCodeUseCase::class
    factoryOf(::GetSavedTargetCurrencyCodeUseCaseImpl) bind GetSavedTargetCurrencyCodeUseCase::class
    factoryOf(::SaveSelectedCurrencyUseCaseImpl) bind SaveSelectedCurrencyUseCase::class
    factoryOf(::ConvertCurrenciesUseCaseImpl) bind ConvertCurrenciesUseCase::class
}