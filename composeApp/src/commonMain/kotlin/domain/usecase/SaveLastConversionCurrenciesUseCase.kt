package domain.usecase

import domain.model.CurrencyType

interface SaveLastConversionCurrenciesUseCase {

    suspend operator fun invoke(currencyType: CurrencyType)
}