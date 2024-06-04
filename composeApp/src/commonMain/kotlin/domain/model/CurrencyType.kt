package domain.model

sealed class CurrencyType(val currency: CurrencyCode) {
    data class SourceCurrency(val source: CurrencyCode) : CurrencyType(source)

    data class TargetCurrency(val target: CurrencyCode) : CurrencyType(target)

    data object None : CurrencyType(CurrencyCode.BRL)
}