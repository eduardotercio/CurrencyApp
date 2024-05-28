package domain.usecase

import domain.repository.CurrencyRepository
import util.getCurrentMoment
import util.parseToMillis

class RatesStatusUseCaseImpl(
    private val repository: CurrencyRepository
) : RatesStatusUseCase {
    override suspend fun invoke(lastUpdated: String): Boolean {
        val currentTime = if (lastUpdated.isNotEmpty()) {
            parseToMillis(lastUpdated)
        } else getCurrentMoment().toEpochMilliseconds()
        return repository.isDataFresh(currentTime)
    }
}