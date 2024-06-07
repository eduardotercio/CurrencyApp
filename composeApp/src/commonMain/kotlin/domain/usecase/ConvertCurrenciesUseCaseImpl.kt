package domain.usecase

import domain.model.Currency
import domain.model.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConvertCurrenciesUseCaseImpl : ConvertCurrenciesUseCase {
    override suspend fun invoke(
        amount: Double,
        sourceCurrency: Currency?,
        targetCurrency: Currency?
    ): RequestState<Double> {
        return withContext(Dispatchers.Default) {
            if (sourceCurrency != null && targetCurrency != null) {

                val exchangeRate = targetCurrency.value / sourceCurrency.value
                val convertedValue = amount * exchangeRate

                RequestState.Success(convertedValue)
            } else {
                RequestState.Error("Invalid currency passed as value.")
            }
        }
    }


}