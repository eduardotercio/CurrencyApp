package domain.service.local

import domain.model.Currency
import domain.model.DataResponse
import domain.model.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoDBService {
    fun configureRealm()

    suspend fun insertCurrencies(vararg currencies: Currency, retrySave: Boolean = true)

    suspend fun readCurrencies(retryRead: Boolean = true): Flow<RequestState<DataResponse>>

    suspend fun cleanRealm()
}