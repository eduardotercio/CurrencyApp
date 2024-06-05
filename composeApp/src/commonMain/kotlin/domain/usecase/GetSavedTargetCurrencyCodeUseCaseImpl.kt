package domain.usecase

import domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetSavedTargetCurrencyCodeUseCaseImpl(
    private val repository: CurrencyRepository
) : GetSavedTargetCurrencyCodeUseCase {
    override suspend fun invoke(): Flow<String> {
        return repository.getSavedTargetCurrencyCode()
    }
}