package domain.usecase

import domain.model.Currency
import domain.model.RequestState

interface ConvertCurrenciesUseCase {
    suspend operator fun invoke(
        inputAmount: String,
        sourceCurrency: Currency?,
        targetCurrency: Currency?
    ): RequestState<Double>
}