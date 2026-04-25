package lotto.strategy

import lotto.model.LottoNumbers
import lotto.model.LottoTicket
import lotto.service.RandomNumberGenerator

class SemiAutoStrategy(
    private val fixed: LottoNumbers,
) : TicketStrategy {
    init {
        require(fixed.size in 1..<LottoNumbers.FULL_SIZE) { "반자동 입력은 1~${LottoNumbers.FULL_SIZE - 1}개여야 합니다." }
    }

    override fun create(): LottoTicket {
        val remaining = RandomNumberGenerator.pick(LottoNumbers.FULL_SIZE - fixed.size, fixed.numbers)
        return LottoTicket(fixed + LottoNumbers(remaining))
    }
}
