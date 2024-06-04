package domain.usecase

import domain.model.CurrencyType
import domain.repository.CurrencyRepository

class SaveLastConversionCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveLastConversionCurrenciesUseCase {
    override suspend fun invoke(currencyType: CurrencyType) {
        repository.saveSelectedCurrency(currencyType)
    }
}