package domain.usecase

import domain.model.CurrencyType

interface SaveSelectedCurrencyUseCase {

    suspend operator fun invoke(currencyType: CurrencyType)
}