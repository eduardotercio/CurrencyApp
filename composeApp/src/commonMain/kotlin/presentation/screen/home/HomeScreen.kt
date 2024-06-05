package presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import domain.model.CurrencyType
import koinViewModel
import presentation.components.CurrencyPickerDialog
import presentation.components.HomeBody
import presentation.components.HomeHeader
import surfaceColor


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.setEvent(HomeScreenContract.Event.InitializeScreen)
    }

    var isDialogOpen by remember { mutableStateOf(false) }
    var currencyType: CurrencyType by remember { mutableStateOf(CurrencyType.None) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenContract.Effect.OpenCurrencyPickerDialog -> {
                    isDialogOpen = true
                }
            }
        }
    }

    if (isDialogOpen) {
        CurrencyPickerDialog(
            currenciesList = state.currencyValuesList,
            currencyType = currencyType,
            onConfirmSelected = {
                if (currencyType.isSource()) {
                    viewModel.setEvent(
                        HomeScreenContract.Event.SaveSelectedCurrency(
                            CurrencyType.SourceCurrency(it)
                        )
                    )
                } else if (currencyType.isTarget()) {
                    viewModel.setEvent(
                        HomeScreenContract.Event.SaveSelectedCurrency(
                            CurrencyType.TargetCurrency(it)
                        )
                    )
                }
                currencyType = CurrencyType.None
                isDialogOpen = false
            },
            onDismiss = {
                currencyType = CurrencyType.None
                isDialogOpen = false
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        if (!state.isLoading) {
            HomeHeader(
                state = state,
                onRefreshButtonClicked = {
                    viewModel.setEvent(HomeScreenContract.Event.RefreshData)
                },
                onSwitchButtonClicked = {
                    viewModel.setEvent(HomeScreenContract.Event.SwitchConversionCurrencies)
                },
                onCurrencyButtonClicked = {
                    viewModel.setEvent(HomeScreenContract.Event.OnDialogOpened)
                    currencyType = it
                },
            )
            HomeBody(
                state = state
            )
        }
    }
}