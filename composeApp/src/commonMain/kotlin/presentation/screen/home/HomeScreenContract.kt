package presentation.screen.home

import domain.model.Currency
import domain.model.RateStatus
import presentation.screen.base.UiEffect
import presentation.screen.base.UiEvent
import presentation.screen.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object RefreshRates : Event
    }

    sealed interface Effect : UiEffect {

    }

    data class State(
        val currencyList: List<Currency> = listOf(),
        val lastUpdated: String = "",
        val currentFormattedDate: String = "",
        val rateStatus: RateStatus = RateStatus.Stale,
        val isDataFresh: Boolean = false
    ) : UiState
}