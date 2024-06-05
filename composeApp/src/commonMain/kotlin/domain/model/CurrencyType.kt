package domain.model

sealed class CurrencyType(val code: CurrencyCode) {
    data class SourceCurrency(val source: CurrencyCode) : CurrencyType(source)

    data class TargetCurrency(val target: CurrencyCode) : CurrencyType(target)

    data object None : CurrencyType(CurrencyCode.BRL)

    fun isSource() = this is SourceCurrency

    fun isTarget() = this is TargetCurrency
}