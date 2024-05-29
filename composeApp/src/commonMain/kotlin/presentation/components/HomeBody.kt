package presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import currencyapptest.composeapp.generated.resources.Res
import currencyapptest.composeapp.generated.resources.from_text
import currencyapptest.composeapp.generated.resources.to_text
import domain.model.CurrencyCode
import org.jetbrains.compose.resources.stringResource
import presentation.screen.home.HomeScreenContract

@Composable
fun HomeBody(
    state: HomeScreenContract.State
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CurrencyFlagButton(
                currency = CurrencyCode.USD,
                placeHolder = stringResource(Res.string.from_text),
                onClick = {}
            )

            SwitchButton(
                modifier = Modifier.padding(top = 24.dp),
                onClick = {}
            )

            CurrencyFlagButton(
                currency = CurrencyCode.EUR,
                placeHolder = stringResource(Res.string.to_text),
                onClick = {}
            )
        }
    }

}