package lotto.strategy

import lotto.model.LottoTicket

class ManualStrategy(
    private val numbers: List<Int>,
) : TicketStrategy {
    override fun create() = LottoTicket(numbers)
}
