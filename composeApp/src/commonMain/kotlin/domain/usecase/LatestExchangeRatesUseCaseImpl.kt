package domain.usecase

import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LatestExchangeRatesUseCaseImpl(
    private val repository: CurrencyRepository
) : LatestExchangeRatesUseCase {
    override suspend fun invoke(getFromLocal: Boolean): RequestState<DataResponse> {
        return withContext(Dispatchers.Default) {
            repository.getLatestExchangeRates(getFromLocal)
        }
    }
}