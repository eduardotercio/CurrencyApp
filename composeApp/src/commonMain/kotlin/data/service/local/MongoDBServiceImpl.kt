package data.service.local

import domain.model.Currency
import domain.model.DataResponse
import domain.model.RequestState
import domain.service.local.MongoDBService
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoDBServiceImpl : MongoDBService {
    private var realm: Realm? = null

    init {
        configureRealm()
    }

    override fun configureRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration
                .Builder(schema = setOf(Currency::class))
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun insertCurrencies(vararg currencies: Currency, retrySave: Boolean) {
        cleanRealm()
        realm?.write {
            currencies.forEach { currency ->
                copyToRealm(currency)
            }
        } ?: run {
            if (retrySave) {
                configureRealm()
                insertCurrencies(*currencies, retrySave = false)
            }
        }
    }

    override suspend fun readCurrencies(retryRead: Boolean): Flow<RequestState<DataResponse>> {
        return realm?.query<Currency>()
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(
                    DataResponse(
                        meta = null,
                        data = result.list
                    )
                )
            } ?: run {
            if (retryRead) {
                configureRealm()
                readCurrencies(false)
            }
            flow {
                RequestState.Error(message = "Realm not initialized")
            }
        }
    }

    private suspend fun cleanRealm() {
        realm?.write {
            val x = this.query<Currency>()
            delete(x)
        }
    }
}