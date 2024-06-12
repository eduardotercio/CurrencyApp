package domain.usecase

import domain.model.Currency
import domain.model.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConvertCurrenciesUseCaseImpl : ConvertCurrenciesUseCase {
    override suspend fun invoke(
        inputAmount: String,
        sourceCurrency: Currency?,
        targetCurrency: Currency?
    ): RequestState<Double> = withContext(Dispatchers.Default)
    {
        runCatching {
            if (sourceCurrency != null && targetCurrency != null) {
                val amount = (if (inputAmount.last().isDigit()) {
                    inputAmount
                } else if (inputAmount.length == 1) {
                    "$ZERO_AMOUNT"
                } else {
                    inputAmount.dropLast(1)
                }).toDouble()

                val exchangeRate = targetCurrency.value / sourceCurrency.value
                val convertedValue = amount * exchangeRate

                RequestState.Success(convertedValue)
            } else {
                RequestState.Error("Invalid currency passed as value.")
            }
        }.getOrElse {
            RequestState.Error("Error: ${it.message}")
        }
    }

    companion object {
        const val ZERO_AMOUNT = 0.0
    }
}