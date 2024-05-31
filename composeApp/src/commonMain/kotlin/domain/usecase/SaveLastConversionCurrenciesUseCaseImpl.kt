package domain.usecase

import domain.repository.CurrencyRepository

class SaveLastConversionCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveLastConversionCurrenciesUseCase {
    override suspend fun invoke() {
        repository.saveLastConversionCurrencies()
    }
}