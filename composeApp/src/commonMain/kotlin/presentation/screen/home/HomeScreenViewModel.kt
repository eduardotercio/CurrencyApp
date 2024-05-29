package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.RequestState
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.TimeFromLastRequestUseCase
import kotlinx.coroutines.launch
import presentation.screen.base.BaseViewModel

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getTimeFromLastRequestUseCase: TimeFromLastRequestUseCase,
    private val saveLastRequestTimeUseCase: SaveLastRequestTimeUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    init {
        viewModelScope.launch {
            getFormattedDate()
            fetchNewRates()
            checkIfCanRefresh()
        }
    }

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenContract.Event.RefreshData -> {
                    fetchNewRates()
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

    private suspend fun fetchNewRates() {
        val getFromLocal = getTimeFromLastRequestUseCase() < ONE_DAY

        val response = getLatestExchangeRatesUseCase(
            getFromLocal = getFromLocal
        )
        when (response) {
            is RequestState.Success -> {
                if (!getFromLocal) {
                    saveTimestamp()
                }
            }

            is RequestState.Loading -> TODO()
            is RequestState.Idle -> TODO()
            is RequestState.Error -> TODO()
        }
    }

    private suspend fun checkIfCanRefresh() {
        val halfDayPassed = getTimeFromLastRequestUseCase() > HALF_DAY

        if (halfDayPassed) {
            setState {
                copy(
                    isRefreshEnabled = true
                )
            }
        }
    }

    private suspend fun saveTimestamp() {
        saveLastRequestTimeUseCase()
    }

    private companion object {
        const val ONE_DAY = 1
        const val HALF_DAY = 0.5
    }

}