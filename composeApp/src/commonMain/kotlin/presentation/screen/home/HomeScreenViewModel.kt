package presentation.screen.home

import androidx.lifecycle.viewModelScope
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.CurrencyType
import domain.model.RequestState
import domain.usecase.ConvertCurrenciesUseCase
import domain.usecase.CurrentFormattedDateUseCase
import domain.usecase.FilterListFromQueryUseCase
import domain.usecase.GetSavedSourceCurrencyCodeUseCase
import domain.usecase.GetSavedTargetCurrencyCodeUseCase
import domain.usecase.LatestExchangeRatesUseCase
import domain.usecase.SaveLastRequestTimeUseCase
import domain.usecase.SaveLatestExchangeRatesUseCase
import domain.usecase.SaveSelectedCurrencyUseCase
import domain.usecase.TimeFromLastRequestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
    private val getSavedSourceCurrencyCodeUseCase: GetSavedSourceCurrencyCodeUseCase,
    private val getSavedTargetCurrencyCodeUseCase: GetSavedTargetCurrencyCodeUseCase,
    private val saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase,
    private val convertCurrenciesUseCase: ConvertCurrenciesUseCase,
    private val filterListFromQueryUseCase: FilterListFromQueryUseCase
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun setInitialState() = HomeScreenContract.State()

    private suspend fun initializeScreen() {
        viewModelScope.launch {
            val executionTime = measureTime {
                launch { getSavedSelectedCurrencies() }
                getFormattedDate()
                fetchNewRates()
                checkIfCanRefresh()
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
                    saveSelectedCurrencyUseCase(event.currencyType)

                    setState {
                        if (event.currencyType is CurrencyType.SourceCurrency) {
                            copy(
                                sourceCurrency = event.currencyType.code,
                                filteredCurrenciesList = currentState.allCurrenciesList
                            )
                        } else {
                            copy(
                                targetCurrency = event.currencyType.code,
                                filteredCurrenciesList = currentState.allCurrenciesList
                            )
                        }
                    }
                }

                is HomeScreenContract.Event.ConvertSourceToTargetCurrency -> {
                    debouncedConversion(event.amount)
                }

                is HomeScreenContract.Event.OnDialogOpened -> {
                    setEffect {
                        HomeScreenContract.Effect.OpenCurrencyPickerDialog
                    }
                }

                is HomeScreenContract.Event.FilterCurrencyList -> {
                    debouncedSearch(event.query)
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

    private val debouncedConversion = scope.debounce<Double> { amount ->
        viewModelScope.launch {
            with(currentState) {
                val sourceCurrency = allCurrenciesList.find { it.code == sourceCurrency.name }
                val targetCurrency = allCurrenciesList.find { it.code == targetCurrency.name }
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

    private companion object {
        const val ONE_DAY = 1
        const val HALF_DAY = 0.5
    }

}