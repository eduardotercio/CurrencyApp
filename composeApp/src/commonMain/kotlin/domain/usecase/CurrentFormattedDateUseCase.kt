package domain.usecase

interface CurrentFormattedDateUseCase {
    suspend operator fun invoke(): String
}