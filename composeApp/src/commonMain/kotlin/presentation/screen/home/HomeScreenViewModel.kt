package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.ConversionCurrencies
import domain.model.RequestState
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.GetLastConversionCurrenciesUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastConversionCurrenciesUseCaseImpl
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.TimeFromLastRequestUseCase
import kotlinx.coroutines.launch
import presentation.base.BaseViewModel

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getTimeFromLastRequestUseCase: TimeFromLastRequestUseCase,
    private val saveLastRequestTimeUseCase: SaveLastRequestTimeUseCase,
    private val getLastConversionCurrenciesUseCase: GetLastConversionCurrenciesUseCase,
    private val saveLastConversionCurrenciesUseCaseImpl: SaveLastConversionCurrenciesUseCaseImpl
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    init {
        viewModelScope.launch {
            getFormattedDate()
            fetchNewRates()
            getLastConversionCurrencies()
            checkIfCanRefresh()
        }
    }

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenContract.Event.RefreshData -> {
                    fetchNewRates(
                        requestToApi = true
                    )
                }

                is HomeScreenContract.Event.SwitchConversionCurrencies -> {
                    switchConversionCurrencies()
                }

                is HomeScreenContract.Event.ConvertCurrencies -> {
                    // Convert currencies

                    // Save conversionCurrencies
                    saveLastConversionCurrenciesUseCaseImpl(event.conversionCurrencies)
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

    private suspend fun fetchNewRates(requestToApi: Boolean = false) {
        val getFromLocal = if (requestToApi) {
            false
        } else {
            val timeFromLastRequest = getTimeFromLastRequestUseCase()
            timeFromLastRequest < ONE_DAY
        }

        val response = getLatestExchangeRatesUseCase(
            getFromLocal = getFromLocal
        )
        when (response) {
            is RequestState.Success -> {
                setState {
                    copy(
                        currencyList = this.currencyList
                    )
                }
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

        setState {
            copy(
                isRefreshEnabled = false
            )
        }
    }

    private suspend fun getLastConversionCurrencies() {
        val lastConversionCurrencies = getLastConversionCurrenciesUseCase(currentState.currencyList)

        setState {
            copy(
                conversionCurrencies = lastConversionCurrencies
            )
        }
    }

    private fun switchConversionCurrencies() {
        val currentSouce = currentState.conversionCurrencies.source
        val currentTarget = currentState.conversionCurrencies.target

        setState {
            copy(
                conversionCurrencies = ConversionCurrencies(
                    source = currentTarget,
                    target = currentSouce
                )
            )
        }
    }

    private companion object {
        const val ONE_DAY = 1
        const val HALF_DAY = 0.5
    }

}