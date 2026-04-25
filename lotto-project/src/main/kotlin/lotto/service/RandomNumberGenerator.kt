package lotto.service

import lotto.model.LottoNumber
import lotto.model.LottoNumbers

object RandomNumberGenerator {
    private val ALL_NUMBERS = (LottoNumber.MIN..LottoNumber.MAX).map { LottoNumber(it) }

    fun pick(
        count: Int = LottoNumbers.FULL_SIZE,
        filter: List<LottoNumber> = emptyList(),
    ): List<LottoNumber> =
        ALL_NUMBERS
            .filter { it !in filter }
            .shuffled()
            .take(count)
}
