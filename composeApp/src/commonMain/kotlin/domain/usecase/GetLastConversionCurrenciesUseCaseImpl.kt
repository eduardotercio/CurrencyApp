package domain.usecase

import domain.model.ConversionCurrencies
import domain.repository.CurrencyRepository

class GetLastConversionCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
): GetLastConversionCurrenciesUseCase {
    override suspend fun invoke(): ConversionCurrencies {
        return repository.getLastConversionCurrencies()
    }
}