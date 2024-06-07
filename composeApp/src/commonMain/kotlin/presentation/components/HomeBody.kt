package presentation.components

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import currencyapptest.composeapp.generated.resources.Res
import currencyapptest.composeapp.generated.resources.bebas_nue_regular
import headerColor
import org.jetbrains.compose.resources.Font
import presentation.screen.home.HomeScreenContract

@Composable
fun HomeBody(
    state: HomeScreenContract.State,
    sendEvent: (HomeScreenContract.Event) -> Unit,
    amount: String
) {
    val convertedAmount = state.convertedAmount

    val currencyConverterAnimation = object : TwoWayConverter<Double, AnimationVector1D> {
        override val convertFromVector: (AnimationVector1D) -> Double = { vector ->
            vector.value.toDouble()
        }

        override val convertToVector: (Double) -> AnimationVector1D = { value ->
            AnimationVector1D(value.toFloat())
        }
    }

    val animatedExchangeAmount by animateValueAsState(
        targetValue = convertedAmount,
        animationSpec = tween(300),
        typeConverter = currencyConverterAnimation
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${(animatedExchangeAmount * 100).toLong() / 100.0}",
                fontSize = 60.sp,
                fontFamily = FontFamily(Font(Res.font.bebas_nue_regular)),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 24.dp)
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(99.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = headerColor,
                contentColor = Color.White
            ),
            onClick = {
                sendEvent(
                    HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount.toDouble())
                )
            }
        ) {
            Text(
                text = "Convert"
            )
        }
    }
}