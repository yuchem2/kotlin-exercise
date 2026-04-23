package lotto.service

import lotto.constant.BONUS_COUNT
import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.LottoTicket

internal object LottoMachine {
    private fun makeRandomNumbers(count: Int = TICKET_SIZE): List<Int> = (MIN_NUMBER..MAX_NUMBER).shuffled().take(count)

    fun createAutoTicket(): LottoTicket = LottoTicket(makeRandomNumbers())

    fun createManualTicket(numbers: List<Int>): LottoTicket = LottoTicket(numbers)

    fun drawing(): Pair<LottoTicket, List<Int>> {
        val numbers = makeRandomNumbers(TICKET_SIZE + BONUS_COUNT)
        val (winnings, bonus) = numbers.chunked(TICKET_SIZE)
        return Pair(createManualTicket(winnings), bonus)
    }
}
