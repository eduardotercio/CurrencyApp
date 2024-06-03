package domain.usecase

import domain.model.ConversionCurrencies

interface SaveLastConversionCurrenciesUseCase {

    suspend operator fun invoke(conversionCurrencies: ConversionCurrencies)
}