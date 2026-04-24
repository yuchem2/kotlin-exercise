package lotto.strategy

import lotto.model.LottoTicket

interface TicketStrategy {
    fun create(): LottoTicket
}
