package domain.usecase

import domain.model.DataResponse
import domain.model.RequestState

interface LatestExchangeRatesUseCase {
    suspend operator fun invoke(isDataFresh: Boolean): RequestState<DataResponse>
}