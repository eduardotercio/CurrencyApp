package domain.usecase

import domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetSavedSourceCurrencyCodeUseCaseImpl(
    private val repository: CurrencyRepository
) : GetSavedSourceCurrencyCodeUseCase {
    override suspend fun invoke(): Flow<String> {
        return repository.getSavedSourceCurrencyCode()
    }
}