package presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import domain.model.CurrencyType
import kotlinx.coroutines.launch
import presentation.animation.JsonAnimation
import presentation.components.CurrencyPickerDialog
import presentation.components.HomeBody
import presentation.components.HomeHeader
import surfaceColor
import util.BackHandler
import util.appActions
import util.koinViewModel

private const val DEFAULT_VALUE = "100.00"
private const val DOT_CHAR = '.'
private const val MIN_CLICKS_TO_EXIT = 2

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsState()
    fun sendEvent(event: HomeScreenContract.Event) = viewModel.setEvent(event)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var countToCloseScreen by rememberSaveable { mutableStateOf(MIN_CLICKS_TO_EXIT) }
    var snackBarResult by rememberSaveable(countToCloseScreen) { mutableStateOf(SnackbarResult.ActionPerformed) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var closeApp by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        sendEvent(HomeScreenContract.Event.InitializeScreen)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenContract.Effect.OpenCurrencyPickerDialog -> {
                    isDialogOpen = true
                }

                is HomeScreenContract.Effect.SnackbarClosureWarning -> {
                    scope.launch {
                        snackBarResult = snackbarHostState.showSnackbar("Press back again to exit")
                        if (snackBarResult == SnackbarResult.Dismissed) {
                            countToCloseScreen++
                        }
                    }
                }

                is HomeScreenContract.Effect.CloseApp -> {
                    closeApp = true
                }
            }
        }
    }

    var currencyType: CurrencyType by remember { mutableStateOf(CurrencyType.SourceCurrency()) }
    var amount by rememberSaveable { mutableStateOf(DEFAULT_VALUE) }

    if (isDialogOpen) {
        CurrencyPickerDialog(
            filteredCurrenciesList = state.filteredCurrenciesList,
            currencyType = currencyType,
            onValueChange = { query ->
                viewModel.setEvent(HomeScreenContract.Event.FilterCurrencyList(query))
            },
            onConfirmSelected = {
                if (currencyType.isSource()) {
                    sendEvent(
                        HomeScreenContract.Event.SaveSelectedCurrency(
                            CurrencyType.SourceCurrency(it)
                        )
                    )
                } else if (currencyType.isTarget()) {
                    sendEvent(
                        HomeScreenContract.Event.SaveSelectedCurrency(
                            CurrencyType.TargetCurrency(it)
                        )
                    )
                }
                sendEvent(
                    HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount)
                )
                isDialogOpen = false
            },
            onDismiss = {
                isDialogOpen = false
            },
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    JsonAnimation(
                        jsonPath = ("files/circular_progression_anim.json"),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                HomeHeader(
                    state = state,
                    sendEvent = { event ->
                        sendEvent(event)
                    },
                    onCurrencyButtonClicked = {
                        sendEvent(HomeScreenContract.Event.OnDialogOpened)
                        currencyType = it
                    },
                    amount = amount,
                    onAmountValueChanged = { input ->
                        if ((input.count { it == DOT_CHAR } <= 1))
                            amount = input
                        sendEvent(HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount))
                    }
                )
                HomeBody(
                    state = state,
                    amount = amount
                )
            }
        }
    }

    BackHandler {
        countToCloseScreen--
        if (countToCloseScreen == 0) {
            sendEvent(HomeScreenContract.Event.OnCloseApp)
        } else {
            sendEvent(HomeScreenContract.Event.OnShowSnackBarClosureWarning)
        }
    }

    if (closeApp) {
        appActions().finishApp()
    }
}