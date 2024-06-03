package domain.usecase

import domain.model.ConversionCurrencies
import domain.model.Currency
import domain.repository.CurrencyRepository

class GetLastConversionCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
) : GetLastConversionCurrenciesUseCase {
    override suspend fun invoke(currenciesList: List<Currency>): ConversionCurrencies {
        val conversionCurrencies = repository.getLastConversionCurrencies(currenciesList)
        return conversionCurrencies.takeIf { it != null } ?: ConversionCurrencies()
    }
}