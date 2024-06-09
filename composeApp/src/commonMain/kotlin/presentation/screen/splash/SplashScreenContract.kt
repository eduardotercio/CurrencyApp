package presentation.screen.splash

import presentation.base.UiEffect
import presentation.base.UiEvent
import presentation.base.UiState

object SplashScreenContract {
    sealed interface Event : UiEvent {
        data object LoadSplash: Event
    }
    sealed interface Effect : UiEffect {
        data object NavigateToHome : Effect
    }

    data object State : UiState
}