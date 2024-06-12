package util

fun String.formatCurrency(): String {
    val parts = this.split(".")
    val integerPart = parts[0]
    val fractionalPart = if (parts.size > 1) parts[1].padEnd(2, '0') else "00"

    val formattedIntegerPart = integerPart.reversed().chunked(3).joinToString(".").reversed()

    return "$formattedIntegerPart.$fractionalPart"
}