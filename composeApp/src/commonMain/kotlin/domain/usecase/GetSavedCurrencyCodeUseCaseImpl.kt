package domain.usecase

import domain.model.CurrencyCode
import domain.model.CurrencyType
import domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSavedCurrencyCodeUseCaseImpl(
    private val repository: CurrencyRepository
) : GetSavedCurrencyCodeUseCase {
    override suspend fun invoke(currencyType: CurrencyType): CurrencyCode =
        withContext(Dispatchers.Default) {
            val code = repository.getSavedSelectedCurrencyCode(currencyType)
            CurrencyCode.entries.find { it.name == code } ?: CurrencyCode.BRL
        }
}