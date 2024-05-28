package domain.usecase

interface RatesStatusUseCase {
    suspend operator fun invoke(lastUpdated: String): Boolean
}