package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.model.Account
import lotto.model.LottoDraw
import lotto.model.LottoTicket

object LottoService {
    private val machine = LottoMachine

    fun purchaseAndSave(
        account: Account,
        count: Int,
        manualNumbers: List<List<Int>>,
    ): List<LottoTicket> {
        val tickets = purchase(account, count, manualNumbers)
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

    fun endAndSave(account: Account): LottoDraw? {
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) return null

        endDraw(lastRound)
        LottoStore.updateLast(lastRound)
        account.deposit(lastRound.getResult().totalIncome)

        return lastRound
    }

    private fun purchase(
        account: Account,
        count: Int,
        manualNumbers: List<List<Int>>,
    ): List<LottoTicket> {
        require(count >= manualNumbers.size) { "수동 생성할 티켓양이 전체 양을 넘을 수 없습니다." }
        val price = count * TICKET_PRICE

        account.withdraw(price)
        return List(count) { index ->
            if (index < manualNumbers.size) {
                machine.createManualTicket(manualNumbers[index])
            } else {
                machine.createAutoTicket()
            }
        }
    }

    private fun createDraw(
        round: Int,
        tickets: List<LottoTicket>,
    ): LottoDraw = LottoDraw(round, tickets as MutableList<LottoTicket>)

    private fun endDraw(draw: LottoDraw) {
        val (winnings, bonus) = machine.drawing()
        draw.endDraw(winnings, bonus)
    }
}
