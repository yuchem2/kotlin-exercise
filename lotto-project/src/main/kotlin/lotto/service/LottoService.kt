package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.generator.WinningNumberGenerator
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import lotto.model.LottoNumbers
import lotto.model.LottoTickets
import lotto.repository.LottoDrawRepository
import lotto.strategy.NumberStrategy

class LottoService(
    private val accountService: AccountService,
    private val drawRepository: LottoDrawRepository,
) {
    fun purchaseAndSave(strategies: List<NumberStrategy>): LottoTickets {
        val tickets = createTickets(strategies)

        val draws = drawRepository.load()
        val lastDraw = draws.last()
        val updateDraws =
            if (lastDraw == null || lastDraw.isEnded()) {
                draws.append(LottoDraw(draws.lastRound() + 1, tickets))
            } else {
                lastDraw.addTicket(tickets)
                draws.updateLast(lastDraw)
            }
        accountService.withdraw(strategies.size * TICKET_PRICE)
        drawRepository.save(updateDraws)

        return tickets
    }

    fun endAndSave(): LottoDraw? {
        val draws = drawRepository.load()
        val lastDraw = draws.last() ?: return null
        if (lastDraw.isEnded()) return null

        lastDraw.endDraw(WinningNumberGenerator.generate())
        drawRepository.save(draws.updateLast(lastDraw))

        val income = lastDraw.getResult().totalIncome
        if (income > 0) {
            accountService.deposit(income)
        }

        return lastDraw
    }

    fun getAllHistory(): LottoDraws = drawRepository.load()

    fun getHistoryByRound(round: Int): LottoDraw? = drawRepository.load().findByRound(round)

    private fun createTickets(strategies: List<NumberStrategy>): LottoTickets = LottoTickets(strategies.map { LottoNumbers(it.pick()) })
}
