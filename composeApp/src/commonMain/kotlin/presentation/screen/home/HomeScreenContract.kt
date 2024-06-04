package presentation.screen.home

import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.CurrencyType
import presentation.base.UiEffect
import presentation.base.UiEvent
import presentation.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object RefreshData : Event

        data object SwitchConversionCurrencies : Event

        data class ConvertCurrencies(
            val currencyType: CurrencyType
        ) : Event
    }

    sealed interface Effect : UiEffect

    data class State(
        val currencyValuesList: List<Currency> = listOf(),
        val currentFormattedDate: String = "",
        val isRefreshEnabled: Boolean = false,
        val source: CurrencyCode = CurrencyCode.BRL,
        val target: CurrencyCode = CurrencyCode.USD,
    ) : UiState
}