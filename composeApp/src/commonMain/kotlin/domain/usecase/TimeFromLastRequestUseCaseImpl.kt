package domain.usecase

import domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import util.getCurrentTimeInMillis
import util.toInstant

class TimeFromLastRequestUseCaseImpl(
    private val repository: CurrencyRepository
) : TimeFromLastRequestUseCase {
    override suspend fun invoke(): Long {
        return withContext(Dispatchers.Default) {
            val lastRequestTimestamp = repository.getLastRequestTime()
            with(Dispatchers.Default) {
                val currentTimestamp = getCurrentTimeInMillis()

                val lastInstant = toInstant(lastRequestTimestamp)
                val currentInstant = toInstant(currentTimestamp)

                val hoursDifference = currentInstant.minus(lastInstant).inWholeHours

                hoursDifference.takeIf { hoursDifference >= ZERO } ?: ONE_DAY
            }
        }
    }

    companion object {
        const val ONE_DAY = 24L
        private const val ZERO = 0L
    }
}