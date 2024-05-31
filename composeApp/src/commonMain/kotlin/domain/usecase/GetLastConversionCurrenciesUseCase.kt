package domain.usecase

import domain.model.ConversionCurrencies

interface GetLastConversionCurrenciesUseCase {

    suspend operator fun invoke(): ConversionCurrencies
}