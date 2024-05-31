package presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import currencyapptest.composeapp.generated.resources.Res
import currencyapptest.composeapp.generated.resources.app_refreshed
import currencyapptest.composeapp.generated.resources.exchange_illustration
import currencyapptest.composeapp.generated.resources.from_text
import currencyapptest.composeapp.generated.resources.refresh_ic
import currencyapptest.composeapp.generated.resources.refresh_the_app
import currencyapptest.composeapp.generated.resources.to_text
import freshColor
import headerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.screen.home.HomeScreenContract
import staleColor

@Composable
fun HomeHeader(
    state: HomeScreenContract.State,
    onRefreshButtonClicked: () -> Unit,
    onSwitchButtonClicked: () -> Unit
) {
    val conversionCurrencies = state.conversionCurrencies

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(headerColor)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        RatesStatus(
            state = state,
            onRefreshButtonClicked = onRefreshButtonClicked
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CurrencyFlagButton(
                modifier = Modifier.weight(1f),
                currency = conversionCurrencies.source,
                placeHolder = stringResource(Res.string.from_text),
                onClick = {}
            )

            SwitchButton(
                modifier = Modifier.padding(top = 24.dp),
                onClick = onSwitchButtonClicked
            )

            CurrencyFlagButton(
                modifier = Modifier.weight(1f),
                currency = conversionCurrencies.target,
                placeHolder = stringResource(Res.string.to_text),
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        InputValueButton()
    }

}

@Composable
fun RatesStatus(
    state: HomeScreenContract.State,
    onRefreshButtonClicked: () -> Unit
) {
    val dateTime = state.currentFormattedDate
    val isRefreshEnabled = state.isRefreshEnabled

    val refreshText: String
    val refreshColor: Color

    if (isRefreshEnabled) {
        refreshText = stringResource(Res.string.refresh_the_app)
        refreshColor = staleColor
    } else {
        refreshText = stringResource(Res.string.app_refreshed)
        refreshColor = freshColor
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(Res.drawable.exchange_illustration),
                contentDescription = "Exchange Rate Illustration"
            )
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(
                    text = dateTime,
                    color = Color.White
                )
                Text(
                    text = refreshText,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = refreshColor
                )
            }
        }
        if (isRefreshEnabled) {
            IconButton(onClick = onRefreshButtonClicked) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.refresh_ic),
                    contentDescription = "Refresh Icon",
                    tint = staleColor
                )
            }
        }
    }

}