package domain.usecase

import domain.model.Currency
import domain.repository.CurrencyRepository

class SaveLatestExchangeRatesUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveLatestExchangeRatesUseCase {
    override suspend fun invoke(vararg currencies: Currency) {
        return repository.saveLatestExchangeRates(*currencies)
    }
}