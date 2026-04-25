package lotto.strategy

import lotto.model.LottoNumbers
import lotto.model.LottoTicket
import lotto.service.RandomNumberGenerator

class SemiAutoStrategy(
    private val fixed: LottoNumbers.Half,
) : TicketStrategy {
    override fun create(): LottoTicket {
        val remaining = RandomNumberGenerator.pick(LottoNumbers.Full.SIZE - fixed.size, fixed.numbers)
        return LottoTicket(fixed + LottoNumbers.Half(remaining))
    }
}
