package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.RequestState
import domain.usecase.ConvertCurrenciesUseCase
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.GetSavedSourceCurrencyCodeUseCase
import domain.usecase.GetSavedTargetCurrencyCodeUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.SaveLatestExchangeRatesUseCase
import domain.usecase.SaveSelectedCurrencyUseCase
import domain.usecase.TimeFromLastRequestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.base.BaseViewModel
import kotlin.time.measureTime

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val saveLatestExchangeRatesUseCase: SaveLatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getTimeFromLastRequestUseCase: TimeFromLastRequestUseCase,
    private val saveLastRequestTimeUseCase: SaveLastRequestTimeUseCase,
    private val getSavedSourceCurrencyCodeUseCase: GetSavedSourceCurrencyCodeUseCase,
    private val getSavedTargetCurrencyCodeUseCase: GetSavedTargetCurrencyCodeUseCase,
    private val saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase,
    private val convertCurrenciesUseCase: ConvertCurrenciesUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    override fun setInitialState() = HomeScreenContract.State()

    private suspend fun initializeScreen() {
        viewModelScope.launch {
            val executionTime = measureTime {
                launch { getSavedSelectedCurrencies() }
                getFormattedDate()
                fetchNewRates()
                checkIfCanRefresh()
            }

            val remainingDelay = (2000 - executionTime.inWholeMilliseconds).coerceAtLeast(0)
            delay(remainingDelay)

            withContext(Dispatchers.Main) {
                setState {
                    copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun handleEvents(event: HomeScreenContract.Event) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenContract.Event.InitializeScreen -> {
                    initializeScreen()
                }

                is HomeScreenContract.Event.RefreshData -> {
                    fetchNewRates(
                        requestToApi = true
                    )
                }

                is HomeScreenContract.Event.SwitchConversionCurrencies -> {
                    switchConversionCurrencies()
                }

                is HomeScreenContract.Event.SaveSelectedCurrency -> {
                    saveSelectedCurrencyUseCase(event.currencyType)
                }

                is HomeScreenContract.Event.ConvertSourceToTargetCurrency -> {
                    convertCurrencies(event.amount)
                }

                is HomeScreenContract.Event.OnDialogOpened -> {
                    setEffect {
                        HomeScreenContract.Effect.OpenCurrencyPickerDialog
                    }
                }
            }
        }
    }

    private suspend fun getFormattedDate() {
        val currentFormattedDate = getCurrentFormattedDateUseCase()

        withContext(Dispatchers.Main) {
            setState {
                copy(
                    currentFormattedDate = currentFormattedDate
                )
            }
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
                        currencyValuesList = response.data.data
                    )
                }
                if (!getFromLocal) {
                    saveCurrency(response.data.data)
                    saveTimestamp()
                }
            }

            is RequestState.Error -> {

            }
        }

    }

    private suspend fun checkIfCanRefresh() {
        val halfDayPassed = getTimeFromLastRequestUseCase() > HALF_DAY

        if (halfDayPassed) {
            withContext(Dispatchers.Main) {
                setState {
                    copy(
                        isRefreshEnabled = true
                    )
                }
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

    private suspend fun saveCurrency(list: List<Currency>) {
        saveLatestExchangeRatesUseCase.invoke(
            currencies = list.toTypedArray()
        )
    }

    private suspend fun getSavedSelectedCurrencies() = withContext(Dispatchers.IO) {
        launch {
            getSavedSourceCurrencyCodeUseCase().collectLatest { code ->
                val source = CurrencyCode.entries.find { it.name == code }
                    ?: currentState.sourceCurrency
                withContext(Dispatchers.Main) {
                    setState {
                        copy(sourceCurrency = source)
                    }
                }
            }
        }
        launch {
            getSavedTargetCurrencyCodeUseCase().collectLatest { code ->
                val target = CurrencyCode.entries.find { it.name == code }
                    ?: currentState.targetCurrency
                withContext(Dispatchers.Main) {
                    setState {
                        copy(targetCurrency = target)
                    }
                }
            }
        }
    }

    private fun switchConversionCurrencies() {
        val currentSouce = currentState.sourceCurrency
        val currentTarget = currentState.targetCurrency

        setState {
            copy(
                sourceCurrency = currentTarget,
                targetCurrency = currentSouce
            )
        }
    }

    private suspend fun convertCurrencies(amount: Double) {
        with(currentState) {
            val sourceCurrency = currencyValuesList.find { it.code == sourceCurrency.name }
            val targetCurrency = currencyValuesList.find { it.code == targetCurrency.name }
            val convertedAmount = convertCurrenciesUseCase(
                amount = amount,
                sourceCurrency = sourceCurrency,
                targetCurrency = targetCurrency
            )

            withContext(Dispatchers.Main) {
                when (convertedAmount) {
                    is RequestState.Success -> {
                        setState {
                            copy(
                                convertedAmount = convertedAmount.data
                            )
                        }
                    }

                    is RequestState.Error -> {

                    }
                }
            }
        }
    }

    private companion object {
        const val ONE_DAY = 1
        const val HALF_DAY = 0.5
    }

}