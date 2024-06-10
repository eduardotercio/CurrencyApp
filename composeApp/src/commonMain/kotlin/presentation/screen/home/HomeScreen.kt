package presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import domain.model.CurrencyType
import presentation.animation.JsonAnimation
import presentation.components.CurrencyPickerDialog
import presentation.components.HomeBody
import presentation.components.HomeHeader
import surfaceColor
import util.koinViewModel

private const val DEFAULT_VALUE = "100.00"

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsState()
    fun sendEvent(event: HomeScreenContract.Event) = viewModel.setEvent(event)

    LaunchedEffect(true) {
        sendEvent(HomeScreenContract.Event.InitializeScreen)
    }

    var isDialogOpen by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is HomeScreenContract.Effect.OpenCurrencyPickerDialog) {
                isDialogOpen = true
            }
        }
    }

    var currencyType: CurrencyType by remember { mutableStateOf(CurrencyType.None) }
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
                    HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount.toDouble())
                )
                currencyType = CurrencyType.None
                isDialogOpen = false
            },
            onDismiss = {
                currencyType = CurrencyType.None
                isDialogOpen = false
            },
        )
    }
    JsonAnimation(
        jsonPath = ("files/circular_progression_anim.json"),
        modifier = Modifier.fillMaxSize()
    )
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
                    amount = input
                    if (input.isNotEmpty())
                        sendEvent(HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount.toDouble()))
                }
            )
            HomeBody(
                state = state,
                sendEvent = { event ->
                    viewModel.setEvent(event)
                },
                amount = amount
            )
        }
    }
}