package presentation.screen

import domain.model.Currency
import presentation.screen.base.UiEffect
import presentation.screen.base.UiEvent
import presentation.screen.base.UiState

object HomeScreenContract {

    sealed interface Event : UiEvent {
        data object GetString: Event
    }

    sealed interface Effect : UiEffect {

    }

    data class State(
        val currencyList: List<Currency> = listOf()
    ) : UiState
}