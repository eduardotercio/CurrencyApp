package domain.usecase

import domain.repository.CurrencyRepository

class GetLatestExchangeRatesUseCaseImpl(
    private val repository: CurrencyRepository
): GetLatestExchangeRatesUseCase {
    override fun invoke(): String {
        return "Hello World!"
    }
}