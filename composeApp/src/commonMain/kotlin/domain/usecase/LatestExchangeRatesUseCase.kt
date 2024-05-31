package domain.usecase

import domain.model.DataResponse
import domain.model.RequestState

interface LatestExchangeRatesUseCase {
    suspend operator fun invoke(getFromLocal: Boolean): RequestState<DataResponse>
}