package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
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
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = placeHolder,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(currency.flag),
                tint = Color.Unspecified,
                contentDescription = "Country image",
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = currency.name,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(Res.drawable.switch_ic),
            contentDescription = "Switch button"
        )
    }
}

@Composable
fun InputValueButton() {

}