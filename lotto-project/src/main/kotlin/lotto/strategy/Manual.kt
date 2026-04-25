package lotto.strategy

import lotto.model.LottoNumber

internal class Manual(
    private val numbers: List<LottoNumber>,
) : NumberStrategy {
    override fun pick() = numbers
}
