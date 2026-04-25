package lotto.strategy

import lotto.model.LottoNumbers

internal class Manual(
    private val numbers: LottoNumbers,
) : NumberStrategy {
    init {
        numbers.requireFull()
    }

    override fun pick() = numbers
}
