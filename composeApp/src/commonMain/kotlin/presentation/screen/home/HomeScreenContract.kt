package presentation.screen.home

import domain.model.ConversionCurrencies
import domain.model.Currency
import presentation.screen.base.UiEffect
import presentation.screen.base.UiEvent
import presentation.screen.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object RefreshData : Event

        data object SwitchConversionCurrencies: Event
    }

    sealed interface Effect : UiEffect {

    }

    data class State(
        val currencyList: List<Currency> = listOf(),
        val currentFormattedDate: String = "",
        val isRefreshEnabled: Boolean = false,
        val conversionCurrencies: ConversionCurrencies = ConversionCurrencies()
    ) : UiState
}