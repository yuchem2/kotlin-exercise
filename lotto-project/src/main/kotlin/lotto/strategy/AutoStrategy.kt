package lotto.strategy

import lotto.model.LottoTicket
import lotto.service.RandomNumberGenerator

class AutoStrategy : TicketStrategy {
    override fun create(): LottoTicket = LottoTicket(RandomNumberGenerator.pick())
}
