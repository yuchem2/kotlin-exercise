package lotto.service

import lotto.constant.TICKET_PRICE
import lotto.model.Account
import lotto.model.LottoDraw
import lotto.model.LottoTicket

object LottoService {
    private val machine = LottoMachine

    fun purchase(
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

    fun createDraw(
        round: Int,
        tickets: List<LottoTicket>,
    ): LottoDraw {
        val (winningNumbers, bonusNumbers) = machine.drawing()
        return LottoDraw(round, tickets, winningNumbers, bonusNumbers)
    }
}
