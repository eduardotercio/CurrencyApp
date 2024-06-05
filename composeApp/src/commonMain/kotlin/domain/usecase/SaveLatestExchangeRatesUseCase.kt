package domain.usecase

import domain.model.Currency

interface SaveLatestExchangeRatesUseCase {

    suspend operator fun invoke(vararg currencies: Currency)
}