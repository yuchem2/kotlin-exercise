package lotto.strategy

import lotto.model.LottoNumber

interface NumberStrategy {
    fun pick(): List<LottoNumber>

    companion object {
        fun manual(numbers: List<LottoNumber>): NumberStrategy = Manual(numbers)

        fun semiAuto(fixed: List<LottoNumber>): NumberStrategy = SemiAuto(fixed)

        fun auto(): NumberStrategy = Auto()
    }
}
