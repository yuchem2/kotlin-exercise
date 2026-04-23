package lotto.service

import lotto.constant.BONUS_COUNT
import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.LottoTicket

internal object LottoMachine {
    fun createAutoTicket(): LottoTicket = LottoTicket(makeRandomNumbers())

    fun createManualTicket(numbers: List<Int>): LottoTicket = LottoTicket(numbers)

    fun createSemiAutoTicket(numbers: List<Int>): LottoTicket {
        require(numbers.size < TICKET_SIZE) { "반자동 번호는 ${TICKET_SIZE}개 미만이어야 합니다." }
        val remaining = makeRandomNumbers(TICKET_SIZE - numbers.size, numbers)

        return LottoTicket(numbers + remaining)
    }

    fun drawing(): Pair<LottoTicket, List<Int>> {
        val numbers = makeRandomNumbers(TICKET_SIZE + BONUS_COUNT)
        val (winnings, bonus) = numbers.chunked(TICKET_SIZE)
        return Pair(createManualTicket(winnings), bonus)
    }

    private fun makeRandomNumbers(
        count: Int = TICKET_SIZE,
        filter: List<Int> = emptyList(),
    ): List<Int> = (MIN_NUMBER..MAX_NUMBER).filter { it !in filter }.shuffled().take(count)
}
