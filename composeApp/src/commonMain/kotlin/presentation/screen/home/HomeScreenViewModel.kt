package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.CurrencyCode
import domain.model.RequestState
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.GetSavedSourceCurrencyCodeUseCase
import domain.usecase.GetSavedTargetCurrencyCodeUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastConversionCurrenciesUseCase
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.TimeFromLastRequestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.base.BaseViewModel

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getTimeFromLastRequestUseCase: TimeFromLastRequestUseCase,
    private val saveLastRequestTimeUseCase: SaveLastRequestTimeUseCase,
    private val getSavedSourceCurrencyCodeUseCase: GetSavedSourceCurrencyCodeUseCase,
    private val getSavedTargetCurrencyCodeUseCase: GetSavedTargetCurrencyCodeUseCase,
    private val saveLastConversionCurrenciesUseCase: SaveLastConversionCurrenciesUseCase,
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    init {
        viewModelScope.launch {
            getFormattedDate()
            fetchNewRates()
            getSavedSelectedCurrencies()
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
                    saveLastConversionCurrenciesUseCase(event.currencyType)
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

    private suspend fun fetchNewRates(requestToApi: Boolean = false) = withContext(Dispatchers.IO) {
        launch {
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
                            currencyValuesList = this.currencyValuesList
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

    private suspend fun getSavedSelectedCurrencies() = withContext(Dispatchers.IO) {
        launch {
            getSavedSourceCurrencyCodeUseCase().collectLatest { code ->
                val source = CurrencyCode.entries.find { it.name == code }
                    ?: currentState.source
                withContext(Dispatchers.Main) {
                    setState {
                        copy(source = source)
                    }
                }
            }
        }

        launch {
            getSavedTargetCurrencyCodeUseCase().collectLatest { code ->
                val target = CurrencyCode.entries.find { it.name == code }
                    ?: currentState.target
                withContext(Dispatchers.Main) {
                    setState {
                        copy(target = target)
                    }
                }
            }
        }
    }

    private fun switchConversionCurrencies() {
        val currentSouce = currentState.source
        val currentTarget = currentState.target

        setState {
            copy(
                source = currentTarget,
                target = currentSouce
            )
        }
    }

    private companion object {
        const val ONE_DAY = 1
        const val HALF_DAY = 0.5
    }

}