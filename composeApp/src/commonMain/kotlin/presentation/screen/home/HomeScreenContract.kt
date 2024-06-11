package presentation.screen.home

import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.CurrencyType
import presentation.base.UiEffect
import presentation.base.UiEvent
import presentation.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object InitializeScreen : Event
        data object RefreshData : Event

        data object SwitchConversionCurrencies : Event

        data class SaveSelectedCurrency(
            val currencyType: CurrencyType
        ) : Event

        data class ConvertSourceToTargetCurrency(
            val amount: String
        ) : Event

        data object OnDialogOpened : Event

        data class FilterCurrencyList(
            val query: String
        ) : Event

        data object OnShowSnackBarClosureWarning : Event

        data object OnCloseApp : Event
    }

    sealed interface Effect : UiEffect {

        data object OpenCurrencyPickerDialog : Effect

        data object SnackbarClosureWarning : Effect

        data object CloseApp : Effect
    }

    data class State(
        val isLoading: Boolean = true,
        val isRefreshEnabled: Boolean = false,
        val currentFormattedDate: String = "",
        val typedAmount: Double = 100.0,
        val convertedAmount: Double = 0.0,
        val allCurrenciesList: List<Currency> = listOf(),
        val filteredCurrenciesList: List<Currency> = listOf(),
        val sourceCurrency: CurrencyCode = CurrencyCode.BRL,
        val targetCurrency: CurrencyCode = CurrencyCode.USD,
    ) : UiState
}