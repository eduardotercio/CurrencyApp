package domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetSavedSourceCurrencyCodeUseCase {

    suspend operator fun invoke(): Flow<String>
}