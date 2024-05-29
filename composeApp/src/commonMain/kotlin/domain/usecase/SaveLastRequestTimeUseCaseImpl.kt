package domain.usecase

import domain.repository.CurrencyRepository
import util.getCurrentTimeInMillis

class SaveLastRequestTimeUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveLastRequestTimeUseCase {
    override suspend fun invoke() {
        val millis = getCurrentTimeInMillis()
        repository.saveLastRequestTime(millis)
    }
}