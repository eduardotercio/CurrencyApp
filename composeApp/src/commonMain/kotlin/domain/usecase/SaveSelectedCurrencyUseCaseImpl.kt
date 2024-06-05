package domain.usecase

import domain.model.CurrencyType
import domain.repository.CurrencyRepository

class SaveSelectedCurrencyUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveSelectedCurrencyUseCase {
    override suspend fun invoke(currencyType: CurrencyType) {
        repository.saveSelectedCurrency(currencyType)
    }
}