package lotto.service

import lotto.model.LottoNumber
import lotto.model.LottoNumbers

object RandomNumberGenerator {
    fun pick(
        count: Int = LottoNumbers.FULL_SIZE,
        filter: List<LottoNumber> = emptyList(),
    ): List<LottoNumber> =
        LottoNumber.ALL_NUMBERS
            .filter { it !in filter }
            .shuffled()
            .take(count)
}
