package presentation.screen.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import presentation.base.BaseViewModel

class SplashScreenViewModel :
    BaseViewModel<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect>() {
    override fun setInitialState(): SplashScreenContract.State = SplashScreenContract.State

    override fun handleEvents(event: SplashScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is SplashScreenContract.Event.LoadSplash -> {
                    loadSplashAnimation()
                }
            }
        }
    }

    private suspend fun loadSplashAnimation() {
        delay(6000)
        setEffect {
            SplashScreenContract.Effect.NavigateToHome
        }
    }
}