package presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import presentation.screen.base.BaseViewModel

class HomeScreenViewModel :
    BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {

                else -> {}
            }
        }
    }


}