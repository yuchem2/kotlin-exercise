package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.model.LottoDraw
import lotto.model.LottoTicket
import lotto.model.LottoTickets
import lotto.strategy.NumberStrategy

object LottoService {
    fun purchaseAndSave(
        account: AccountStore,
        strategies: List<NumberStrategy>,
    ): LottoTickets {
        val tickets = createTickets(strategies)
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) {
            val draw = createDraw(LottoStore.getLastRound() + 1, tickets)
            LottoStore.save(draw)
        } else {
            lastRound.addTicket(tickets)
            LottoStore.updateLast(lastRound)
        }

        val income: Long = strategies.size * TICKET_PRICE
        account.withdraw(income)

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

    private fun createTickets(strategies: List<NumberStrategy>): LottoTickets {
        val tickets = strategies.map { LottoTicket.create(it) }
        return LottoTickets(tickets)
    }

    private fun createDraw(
        round: Int,
        tickets: LottoTickets,
    ): LottoDraw = LottoDraw(round, tickets)

    private fun endDraw(draw: LottoDraw) = draw.endDraw(WinningNumberGenerator.generate())
}
