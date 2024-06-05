package domain.usecase

import domain.repository.CurrencyRepository
import util.getCurrentTimeInMillis
import util.parseFromMillis

class TimeFromLastRequestUseCaseImpl(
    private val repository: CurrencyRepository
) : TimeFromLastRequestUseCase {
    override suspend fun invoke(): Double {
        val currentTimestamp = getCurrentTimeInMillis()
        val lastRequestTimestamp = repository.getLastRequestTime()

        val currentDateTime = parseFromMillis(currentTimestamp)
        val lastRequestDateTime = parseFromMillis(lastRequestTimestamp)

        if (lastRequestDateTime.year != currentDateTime.year)
            return ONE.toDouble()
        val daysDifference = (currentDateTime.dayOfYear - lastRequestDateTime.dayOfYear)
        return daysDifference.toDouble()
    }

    private companion object {
        const val ONE = 1
    }
}