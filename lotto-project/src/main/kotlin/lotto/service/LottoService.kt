package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.model.LottoDraw
import lotto.model.LottoTicket

object LottoService {
    private val machine = LottoMachine

    fun purchaseAndSave(
        account: AccountStore,
        count: Int,
        manualNumbers: List<List<Int>>,
        semiAutoNumbers: List<List<Int>>,
    ): List<LottoTicket> {
        val tickets = purchase(account, count, manualNumbers, semiAutoNumbers)
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
        count: Int,
        manualNumbers: List<List<Int>>,
        semiAutoNumbers: List<List<Int>>,
    ): List<LottoTicket> {
        val price = count * TICKET_PRICE

        val tickets =
            List(count) { index ->
                when {
                    index < manualNumbers.size -> {
                        machine.createManualTicket(manualNumbers[index])
                    }

                    index - manualNumbers.size < semiAutoNumbers.size -> {
                        machine.createSemiAutoTicket(semiAutoNumbers[index - manualNumbers.size])
                    }

                    else -> {
                        machine.createAutoTicket()
                    }
                }
            }
        account.withdraw(price)
        return tickets
    }

    private fun createDraw(
        round: Int,
        tickets: List<LottoTicket>,
    ): LottoDraw = LottoDraw(round, tickets.toMutableList())

    private fun endDraw(draw: LottoDraw) {
        val (winnings, bonus) = machine.drawing()
        draw.endDraw(winnings, bonus)
    }
}
