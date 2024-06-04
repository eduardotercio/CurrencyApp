package presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import currencyapptest.composeapp.generated.resources.Res
import currencyapptest.composeapp.generated.resources.switch_ic
import domain.model.CurrencyCode
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyFlagButton(
    modifier: Modifier = Modifier,
    currency: CurrencyCode,
    placeHolder: String,
    onClick: () -> Unit,
    animate: Boolean,
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
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = currency,
                transitionSpec = {
                    if (animate) transitionSpec
                    else ContentTransform(EnterTransition.None, ExitTransition.None)
                },
                label = "Content Animation"
            ) {
                val flag = remember(it) {
                    CurrencyCode.entries.first { code ->
                        code.name == currency.name
                    }.flag
                }
                val name by remember { mutableStateOf(it.name) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(flag),
                        tint = Color.Unspecified,
                        contentDescription = "Country image",
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            onClick()
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
fun InputValueButton() {
    var value by remember { mutableStateOf(TextFieldValue("$DEFAULT_VALUE")) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 8.dp))
            .animateContentSize(),
        value = value,
        onValueChange = { newValue ->
            value = newValue
        },
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            color = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.05f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            errorContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        )
    )
}

private const val DEFAULT_VALUE = 100.00