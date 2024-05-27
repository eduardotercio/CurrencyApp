package presentation.screen

import androidx.lifecycle.viewModelScope
import domain.usecase.GetLatestExchangeRatesUseCase
import kotlinx.coroutines.launch
import presentation.screen.base.BaseViewModel

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: GetLatestExchangeRatesUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenContract.Event.GetString -> {
                    getString()
                }
            }
        }
    }

    private fun getString() {
        val string = getLatestExchangeRatesUseCase()
        println("DuduData: $string")
    }

}