package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.RateStatus
import domain.model.RequestState
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.RatesStatusUseCase
import domain.usecase.SaveCurrentMetaDataUseCase
import kotlinx.coroutines.launch
import presentation.screen.base.BaseViewModel

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getRatesStatusUseCase: RatesStatusUseCase,
    private val saveCurrentMetaDataUseCase: SaveCurrentMetaDataUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    init {
        viewModelScope.launch {
            getFormattedDate()
            fetchNewRates()
            getRatesStatus()
        }
    }

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenContract.Event.RefreshRates -> {
                    getRatesStatus()
                }
            }
        }
    }

    private fun getFormattedDate() {
        val currentFormattedDate = getCurrentFormattedDateUseCase()

        setState {
            copy(
                currentFormattedDate = currentFormattedDate
            )
        }
    }

    private suspend fun getRatesStatus() {
        val isDataFresh = getRatesStatusUseCase(currentState.lastUpdated)
        val rateStatus =
            if (isDataFresh) RateStatus.Fresh
            else RateStatus.Stale

        setState {
            copy(
                rateStatus = rateStatus,
                isDataFresh = isDataFresh
            )
        }
    }

    private suspend fun fetchNewRates() {
        val response = getLatestExchangeRatesUseCase(currentState.isDataFresh)
        when (response) {
            is RequestState.Success -> {
                saveTimestamp(response.data.meta.lastTimeUpdated)
            }

            is RequestState.Loading -> TODO()
            is RequestState.Idle -> TODO()
            is RequestState.Error -> TODO()
        }
    }

    private suspend fun saveTimestamp(lastUpdated: String) {
        saveCurrentMetaDataUseCase(lastUpdated)
        setState {
            copy(
                lastUpdated = lastUpdated
            )
        }
    }

}