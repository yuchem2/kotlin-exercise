package lotto.service

import lotto.constant.BONUS_COUNT
import lotto.constant.TICKET_SIZE
import lotto.model.LottoTicket
import lotto.model.WinningNumbers

object WinningNumberGenerator {
    fun generate(): WinningNumbers {
        val numbers = RandomNumberGenerator.pick(TICKET_SIZE + BONUS_COUNT)
        val (winnings, bonus) = numbers.chunked(TICKET_SIZE)
        return WinningNumbers(LottoTicket(winnings), bonus)
    }
}
