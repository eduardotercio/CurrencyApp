package presentation.screen.home

import domain.model.ConversionCurrencies
import domain.model.Currency
import presentation.base.UiEffect
import presentation.base.UiEvent
import presentation.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object RefreshData : Event

        data object SwitchConversionCurrencies : Event

        data class ConvertCurrencies(
            val conversionCurrencies: ConversionCurrencies
        ): Event
    }

    sealed interface Effect : UiEffect

    data class State(
        val currencyList: List<Currency> = listOf(),
        val currentFormattedDate: String = "",
        val isRefreshEnabled: Boolean = false,
        val conversionCurrencies: ConversionCurrencies = ConversionCurrencies()
    ) : UiState
}