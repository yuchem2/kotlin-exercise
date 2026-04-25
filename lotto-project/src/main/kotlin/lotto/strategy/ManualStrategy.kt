package lotto.strategy

import lotto.model.LottoNumbers
import lotto.model.LottoTicket

class ManualStrategy(
    private val numbers: LottoNumbers,
) : TicketStrategy {
    init {
        numbers.requireFull()
    }

    override fun create() = LottoTicket(numbers)
}
