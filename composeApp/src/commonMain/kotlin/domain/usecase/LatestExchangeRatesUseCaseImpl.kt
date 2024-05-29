package domain.usecase

import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository

class LatestExchangeRatesUseCaseImpl(
    private val repository: CurrencyRepository
) : LatestExchangeRatesUseCase {
    override suspend fun invoke(getFromLocal: Boolean): RequestState<DataResponse> {
        return repository.getLatestExchangeRates(getFromLocal)
    }
}