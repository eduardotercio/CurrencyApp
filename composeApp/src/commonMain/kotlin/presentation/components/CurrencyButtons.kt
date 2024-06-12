package presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import currencyapptest.composeapp.generated.resources.Res
import currencyapptest.composeapp.generated.resources.switch_ic
import domain.model.CurrencyCode
import org.jetbrains.compose.resources.painterResource
import presentation.screen.home.HomeScreenContract

private const val CHAR_INPUT_LIMIT = 15

@Composable
fun CurrencyDisplayButton(
    modifier: Modifier = Modifier,
    currency: CurrencyCode,
    placeHolder: String,
    onClick: () -> Unit,
    transitionSpec: ContentTransform = scaleIn(tween(durationMillis = 400))
            + fadeIn(tween(durationMillis = 800))
            togetherWith scaleOut(tween(durationMillis = 400))
            + fadeOut(tween(durationMillis = 800))
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = placeHolder,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = Color.White
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .clickable { onClick() }
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = currency,
                transitionSpec = { transitionSpec },
                label = "Content Animation"
            ) {
                CurrencyFlag(
                    currencyCode = it
                )
            }
        }
    }
}

@Composable
fun CurrencyFlag(
    currencyCode: CurrencyCode,
    flagColorMatrix: ColorMatrix = ColorMatrix(),
    textAlpha: Float = 1f
) {
    val flag = remember(currencyCode) {
        CurrencyCode.entries.first { code ->
            code.name == currencyCode.name
        }.flag
    }
    val name by remember(currencyCode) { mutableStateOf(currencyCode.name) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(flag),
            contentDescription = "Country image",
            colorFilter = ColorFilter.colorMatrix(flagColorMatrix)
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            modifier = Modifier.alpha(textAlpha),
            text = name,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun SwitchButton(
    amount: String,
    modifier: Modifier = Modifier,
    sendEvent: (HomeScreenContract.Event) -> Unit
) {
    var animatedStarted by remember { mutableStateOf(false) }
    val animatedRotation by animateFloatAsState(
        targetValue = if (animatedStarted) 180f else 0f,
        animationSpec = tween(500)
    )

    IconButton(
        modifier = modifier
            .graphicsLayer {
                rotationY = animatedRotation
            },
        onClick = {
            animatedStarted = !animatedStarted
            sendEvent(HomeScreenContract.Event.SwitchConversionCurrencies)
            sendEvent(HomeScreenContract.Event.ConvertSourceToTargetCurrency(amount))
        }
    ) {
        Icon(
            painter = painterResource(Res.drawable.switch_ic),
            contentDescription = "Switch button",
            tint = Color.White
        )
    }
}

@Composable
fun InputValueButton(
    amount: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 8.dp))
            .animateContentSize(),
        value = amount,
        onValueChange = { newValue ->
            if (newValue.length <= CHAR_INPUT_LIMIT)
                onValueChanged(newValue)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.05f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            errorContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            color = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}