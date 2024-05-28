package domain.usecase

import domain.repository.CurrencyRepository
import util.parseToMillis

class SaveCurrentMetaDataUseCaseImpl(
    private val repository: CurrencyRepository
) : SaveCurrentMetaDataUseCase {
    override suspend fun invoke(lastTimeUpdated: String) {
        val millis = parseToMillis(lastTimeUpdated)
        repository.saveTimestamp(millis)
    }
}