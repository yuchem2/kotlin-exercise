package lotto.strategy

import lotto.model.LottoNumbers

interface NumberStrategy {
    fun pick(): LottoNumbers

    companion object {
        fun manual(numbers: LottoNumbers): NumberStrategy = Manual(numbers)

        fun semiAuto(fixed: LottoNumbers): NumberStrategy = SemiAuto(fixed)

        fun auto(): NumberStrategy = Auto()
    }
}
