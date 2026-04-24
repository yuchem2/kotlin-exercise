package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.model.LottoDraw
import lotto.model.LottoTickets
import lotto.strategy.TicketStrategy

object LottoService {
    fun purchaseAndSave(
        account: AccountStore,
        strategies: List<TicketStrategy>,
    ): LottoTickets {
        val tickets = purchase(account, strategies)
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) {
            val draw = createDraw(LottoStore.getLastRound() + 1, tickets)
            LottoStore.save(draw)
        } else {
            lastRound.addTicket(tickets)
            LottoStore.updateLast(lastRound)
        }
        return tickets
    }

    fun endAndSave(account: AccountStore): LottoDraw? {
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) return null

        endDraw(lastRound)
        LottoStore.updateLast(lastRound)

        val income = lastRound.getResult().totalIncome
        if (income > 0) account.deposit(income)

        return lastRound
    }

    private fun purchase(
        account: AccountStore,
        strategies: List<TicketStrategy>,
    ): LottoTickets {
        val tickets = LottoTickets(strategies.map { it.create() })

        account.withdraw(strategies.size * TICKET_PRICE)
        return tickets
    }

    private fun createDraw(
        round: Int,
        tickets: LottoTickets,
    ): LottoDraw = LottoDraw(round, tickets)

    private fun endDraw(draw: LottoDraw) = draw.endDraw(WinningNumberGenerator.generate())
}
