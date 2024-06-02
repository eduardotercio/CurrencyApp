package domain.usecase

import domain.model.ConversionCurrencies
import domain.repository.CurrencyRepository

class SaveLastConversionCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveLastConversionCurrenciesUseCase {
    override suspend fun invoke(conversionCurrencies: ConversionCurrencies) {
        repository.saveLastConversionCurrencies(conversionCurrencies)
    }
}