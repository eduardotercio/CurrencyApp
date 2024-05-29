package domain.usecase

interface TimeFromLastRequestUseCase {
    suspend operator fun invoke(): Double
}