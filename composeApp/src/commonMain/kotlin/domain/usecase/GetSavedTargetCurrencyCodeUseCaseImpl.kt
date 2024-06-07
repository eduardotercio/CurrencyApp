package domain.usecase

import domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetSavedTargetCurrencyCodeUseCaseImpl(
    private val repository: CurrencyRepository
) : GetSavedTargetCurrencyCodeUseCase {
    override suspend fun invoke(): Flow<String> {
        return withContext(Dispatchers.IO) { repository.getSavedTargetCurrencyCode() }
    }
}