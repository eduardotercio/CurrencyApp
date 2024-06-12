package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.Currency
import domain.model.CurrencyType
import domain.model.RequestState
import domain.usecase.ConvertCurrenciesUseCase
import domain.usecase.ConvertCurrenciesUseCaseImpl.Companion.ZERO_AMOUNT
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.FilterListFromQueryUseCase
import domain.usecase.GetSavedCurrencyCodeUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.SaveLatestExchangeRatesUseCase
import domain.usecase.SaveSelectedCurrencyUseCase
import domain.usecase.TimeFromLastRequestUseCase
import domain.usecase.TimeFromLastRequestUseCaseImpl.Companion.ONE_DAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.base.BaseViewModel
import util.debounce
import kotlin.time.measureTime

class HomeScreenViewModel(
    private val getLatestExchangeRatesUseCase: LatestExchangeRatesUseCase,
    private val saveLatestExchangeRatesUseCase: SaveLatestExchangeRatesUseCase,
    private val getCurrentFormattedDateUseCase: CurrentFormattedDateUseCase,
    private val getTimeFromLastRequestUseCase: TimeFromLastRequestUseCase,
    private val saveLastRequestTimeUseCase: SaveLastRequestTimeUseCase,
    private val getSavedCurrencyCodeUseCase: GetSavedCurrencyCodeUseCase,
    private val saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase,
    private val convertCurrenciesUseCase: ConvertCurrenciesUseCase,
    private val filterListFromQueryUseCase: FilterListFromQueryUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun setInitialState() = HomeScreenContract.State()

    private suspend fun initializeScreen() {
        viewModelScope.launch {
            val executionTime = measureTime {
                getSavedSelectedCurrencies()
                getFormattedDate()
                fetchNewRates()
                checkIfCanRefresh()
                debouncedConversion(DEFAULT_VALUE)
            }

            val remainingDelay = (2500 - executionTime.inWholeMilliseconds).coerceAtLeast(0)
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
                    if (areCurrenciesEqual(event.currencyType)) {
                        switchConversionCurrencies()
                        resetFilteredList()
                    } else {
                        saveCurrency(event.currencyType)
                    }
                }

                is HomeScreenContract.Event.ConvertSourceToTargetCurrency -> {
                    debouncedConversion(event.inputAmount)
                }

                is HomeScreenContract.Event.OnDialogOpened -> {
                    setEffect {
                        HomeScreenContract.Effect.OpenCurrencyPickerDialog
                    }
                }

                is HomeScreenContract.Event.FilterCurrencyList -> {
                    debouncedSearch(event.query)
                }

                is HomeScreenContract.Event.OnShowSnackBarClosureWarning -> {
                    setEffect {
                        HomeScreenContract.Effect.SnackbarClosureWarning
                    }
                }

                is HomeScreenContract.Event.OnCloseApp -> {
                    setEffect {
                        HomeScreenContract.Effect.CloseApp
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
                        allCurrenciesList = response.data.data,
                        filteredCurrenciesList = response.data.data
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

    private suspend fun getSavedSelectedCurrencies() {
        val source = getSavedCurrencyCodeUseCase(CurrencyType.SourceCurrency())
        val target = getSavedCurrencyCodeUseCase(CurrencyType.TargetCurrency())

        withContext(Dispatchers.Main) {
            setState {
                copy(
                    sourceCurrency = source,
                    targetCurrency = target
                )
            }
        }
    }

    private suspend fun switchConversionCurrencies() {
        val currentSouce = currentState.sourceCurrency
        val currentTarget = currentState.targetCurrency

        withContext(Dispatchers.Main) {
            setState {
                copy(
                    sourceCurrency = currentTarget,
                    targetCurrency = currentSouce
                )
            }
        }
    }

    private suspend fun saveCurrency(currencyType: CurrencyType) {
        saveSelectedCurrencyUseCase(currencyType)

        withContext(Dispatchers.Main) {
            setState {
                if (currencyType.isSource()) {
                    copy(
                        sourceCurrency = currencyType.currencyCode!!
                    )
                } else {
                    copy(
                        targetCurrency = currencyType.currencyCode!!
                    )
                }
            }
            resetFilteredList()
        }
    }

    private fun areCurrenciesEqual(currencyType: CurrencyType): Boolean {
        return if (currencyType.isSource()) {
            currencyType.currencyCode!!.name == currentState.targetCurrency.name
        } else {
            currencyType.currencyCode!!.name == currentState.sourceCurrency.name
        }
    }

    private fun resetFilteredList() {
        setState {
            copy(
                filteredCurrenciesList = currentState.allCurrenciesList
            )
        }
    }

    private val debouncedConversion = scope.debounce<String> { inputAmount ->
        viewModelScope.launch {
            with(currentState) {
                val convertedAmount = if (inputAmount.isEmpty()) {
                    RequestState.Success(ZERO_AMOUNT)
                } else {
                    val sourceCurrency = allCurrenciesList.find { it.code == sourceCurrency.name }
                    val targetCurrency = allCurrenciesList.find { it.code == targetCurrency.name }
                    convertCurrenciesUseCase(
                        inputAmount = inputAmount,
                        sourceCurrency = sourceCurrency,
                        targetCurrency = targetCurrency
                    )
                }
                withContext(Dispatchers.Main) {
                    when (convertedAmount) {
                        is RequestState.Success -> {
                            if (!convertedAmount.data.isNaN()) {
                                setState {
                                    copy(
                                        convertedAmount = convertedAmount.data
                                    )
                                }
                            }
                        }

                        is RequestState.Error -> {}
                    }
                }
            }
        }
    }

    private val debouncedSearch = scope.debounce<String> { query ->
        viewModelScope.launch {
            val filteredList = filterListFromQueryUseCase(currentState.allCurrenciesList, query)
            withContext(Dispatchers.Main) {
                setState {
                    copy(
                        filteredCurrenciesList = filteredList
                    )
                }
            }
        }
    }

    companion object {
        private const val HALF_DAY = 12L
        const val DEFAULT_VALUE = "1"
    }
}