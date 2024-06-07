package domain.usecase

import domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import util.getCurrentTimeInMillis
import util.parseFromMillis

class TimeFromLastRequestUseCaseImpl(
    private val repository: CurrencyRepository
) : TimeFromLastRequestUseCase {
    override suspend fun invoke(): Double {
        return withContext(Dispatchers.IO) {
            val lastRequestTimestamp = repository.getLastRequestTime()
            with(Dispatchers.Default) {
                val currentTimestamp = getCurrentTimeInMillis()

                val lastRequestDateTime = parseFromMillis(lastRequestTimestamp)
                val currentDateTime = parseFromMillis(currentTimestamp)

                if (lastRequestDateTime.year != currentDateTime.year)
                    ONE.toDouble()

                val daysDifference = (currentDateTime.dayOfYear - lastRequestDateTime.dayOfYear)
                daysDifference.toDouble()
            }
        }
    }

    private companion object {
        const val ONE = 1
    }
}