package domain.usecase

interface GetLatestExchangeRatesUseCase {
    operator fun invoke(): String
}