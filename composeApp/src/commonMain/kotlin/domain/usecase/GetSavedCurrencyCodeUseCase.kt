package domain.usecase

import domain.model.CurrencyCode
import domain.model.CurrencyType

interface GetSavedCurrencyCodeUseCase {

    suspend operator fun invoke(currencyType: CurrencyType): CurrencyCode
}