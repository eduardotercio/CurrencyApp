package util

fun String.formatCurrency(): String {
    val parts = this.split(".")
    val integerPart = parts[0]
    val fractionalPart = parts[1]

    val formattedFractionalPart = fractionalPart.padEnd(2, '0')
    val formattedIntegerPart = integerPart.reversed().chunked(3).joinToString(".").reversed()

    return "$formattedIntegerPart.$formattedFractionalPart"
}