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

        val daysDifference = (currentDateTime.date.dayOfYear - lastRequestDateTime.date.dayOfYear)
        return daysDifference.toDouble()
    }
}