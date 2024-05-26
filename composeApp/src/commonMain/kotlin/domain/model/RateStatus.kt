package domain.model

import androidx.compose.ui.graphics.Color
import freshColor
import staleColor

enum class RateStatus(
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = Color.White
    ),
    Fresh(
        title = "Fresh Rates",
        color = freshColor
    ),
    Stale(
        title = "Rates are not Fresh",
        color = staleColor
    )
}
