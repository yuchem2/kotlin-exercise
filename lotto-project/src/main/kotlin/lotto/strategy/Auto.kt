package lotto.strategy

import lotto.model.LottoNumbers
import lotto.service.RandomNumberGenerator

internal class Auto : NumberStrategy {
    override fun pick(): LottoNumbers = LottoNumbers(RandomNumberGenerator.pick())
}
