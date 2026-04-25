package lotto.strategy

import lotto.model.LottoNumbers
import lotto.service.RandomNumberGenerator

internal class SemiAuto(
    private val fixed: LottoNumbers,
) : NumberStrategy {
    init {
        require(fixed.size in 1..<LottoNumbers.FULL_SIZE) { "반자동 입력은 1~${LottoNumbers.FULL_SIZE - 1}개여야 합니다." }
    }

    override fun pick(): LottoNumbers {
        val remaining = RandomNumberGenerator.pick(LottoNumbers.FULL_SIZE - fixed.size, fixed.numbers)
        return fixed + LottoNumbers(remaining)
    }
}
