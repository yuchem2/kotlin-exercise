package lotto.service

import lotto.model.LottoNumber
import lotto.model.LottoNumbers

object RandomNumberGenerator {
    fun pick(
        count: Int = LottoNumbers.Full.SIZE,
        filter: List<LottoNumber> = emptyList(),
    ): List<LottoNumber> =
        (LottoNumber.MIN..LottoNumber.MAX)
            .map { LottoNumber(it) }
            .filter { it !in filter }
            .shuffled()
            .take(count)
}
