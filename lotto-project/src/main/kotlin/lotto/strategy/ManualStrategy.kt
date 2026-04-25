package lotto.strategy

import lotto.model.LottoNumbers
import lotto.model.LottoTicket

class ManualStrategy(
    private val numbers: LottoNumbers.Full,
) : TicketStrategy {
    override fun create() = LottoTicket(numbers)
}
