package domain.usecase

import domain.model.ConversionCurrencies
import domain.model.Currency

interface GetLastConversionCurrenciesUseCase {

    suspend operator fun invoke(currenciesList: List<Currency>): ConversionCurrencies
}