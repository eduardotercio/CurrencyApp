package domain.usecase

import domain.model.Currency
import domain.model.RequestState

interface ConvertCurrenciesUseCase {
    suspend operator fun invoke(
        amount: Double,
        sourceCurrency: Currency?,
        targetCurrency: Currency?
    ): RequestState<Double>
}