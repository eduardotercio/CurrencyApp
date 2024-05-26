package data.repository

import domain.model.DataResponse
import domain.model.RequestState
import domain.repository.CurrencyRepository
import domain.service.local.PreferencesService
import domain.service.remote.CurrencyApiService
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApiService,
    private val preferences: PreferencesService,
    private val currentTimestamp: Long
) : CurrencyRepository {

    override suspend fun getLatestExchangeRates(): RequestState<DataResponse> {
        val localTimestamp = preferences.getLastUpdated()
        val isDataFresh = isLocalTimestampFresh(localTimestamp)

        return if (isDataFresh) {
            TODO()
        } else {
            currencyApi.getLatestExchangeRates()
        }
    }

    private fun isLocalTimestampFresh(localTimestamp: Long): Boolean {
        return if (localTimestamp == ZERO) {
            false
        } else {
            val currentDateTime = Instant.fromEpochMilliseconds(currentTimestamp).toLocalDateTime(
                TimeZone.currentSystemDefault()
            )
            val localDateTime = Instant.fromEpochMilliseconds(localTimestamp).toLocalDateTime(
                TimeZone.currentSystemDefault()
            )

            (currentDateTime.date.dayOfYear - localDateTime.date.dayOfYear) < ONE
        }
    }

    override suspend fun saveTimestamp(timestampUpdated: String) =
        preferences.saveLastUpdated(timestampUpdated)


    private companion object {
        const val ZERO = 0L
        const val ONE = 1L
    }
}