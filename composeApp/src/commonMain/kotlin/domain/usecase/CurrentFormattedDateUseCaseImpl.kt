package domain.usecase

import util.getCurrentLocalDateTime

class CurrentFormattedDateUseCaseImpl : CurrentFormattedDateUseCase {
    override fun invoke(): String {
        val currentLocalDateTime = getCurrentLocalDateTime()
        val day = currentLocalDateTime.dayOfMonth
        val suffix = when (day % 10) {
            1 -> if (day == 11) "th" else "st"
            2 -> if (day == 12) "th" else "nd"
            3 -> if (day == 13) "th" else "rd"
            else -> "th"
        }

        val month = currentLocalDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val year = currentLocalDateTime.year

        return "$day$suffix $month, $year"
    }
}