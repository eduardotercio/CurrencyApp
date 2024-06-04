package domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetSavedTargetCurrencyCodeUseCase {

    suspend operator fun invoke(): Flow<String>
}