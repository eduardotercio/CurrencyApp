package domain.usecase

import domain.model.Currency

interface FilterListFromQueryUseCase {

    suspend operator fun invoke(list: List<Currency>, query: String): List<Currency>
}