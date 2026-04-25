package lotto.strategy

import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.generator.RandomNumberGenerator

internal class SemiAuto(
    private val fixed: List<LottoNumber>,
) : NumberStrategy {
    init {
        require(fixed.size in 1..<LottoNumbers.FULL_SIZE) { "반자동 입력은 1~${LottoNumbers.FULL_SIZE - 1}개여야 합니다." }
    }

    override fun pick(): List<LottoNumber> {
        val remaining = RandomNumberGenerator.pick(LottoNumbers.FULL_SIZE - fixed.size, fixed)
        return fixed + remaining
    }
}
