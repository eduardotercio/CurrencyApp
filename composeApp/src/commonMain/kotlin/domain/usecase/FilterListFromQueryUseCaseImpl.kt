package domain.usecase

import domain.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterListFromQueryUseCaseImpl:FilterListFromQueryUseCase {
    override suspend fun invoke(list: List<Currency>, query: String): List<Currency> {
        return withContext(Dispatchers.Default) {
            val filteredList = mutableListOf<Currency>()

            if (query.isNotEmpty()) {
                val queryLength = query.length
                val filteredCurrencies = list.filter { currency ->
                    currency.code.length >= queryLength && currency.code.startsWith(query)
                }
                filteredList.addAll(filteredCurrencies)
            } else {
                filteredList.addAll(list)
            }
            filteredList
        }
    }
}