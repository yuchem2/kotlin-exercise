package lotto.service

import lotto.constant.BONUS_COUNT
import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.LottoTicket
import lotto.model.WinningNumbers

internal object LottoMachine {
    fun createAutoTicket(): LottoTicket = LottoTicket(makeRandomNumbers())

    fun createManualTicket(numbers: List<Int>): LottoTicket = LottoTicket(numbers)

    fun createSemiAutoTicket(numbers: List<Int>): LottoTicket {
        require(numbers.size < TICKET_SIZE) { "반자동 번호는 ${TICKET_SIZE}개 미만이어야 합니다." }
        require(numbers.isNotEmpty()) { "반자동 번호는 1개 이상이여야 합니다." }
        require(numbers.all { it in MIN_NUMBER..MAX_NUMBER }) { "로또 번호는 ${MIN_NUMBER}와 $MAX_NUMBER 사이여야 합니다" }

        val remaining = makeRandomNumbers(TICKET_SIZE - numbers.size, numbers)
        return LottoTicket(numbers + remaining)
    }

    fun drawing(): WinningNumbers {
        val numbers = makeRandomNumbers(TICKET_SIZE + BONUS_COUNT)
        val (winnings, bonus) = numbers.chunked(TICKET_SIZE)
        return WinningNumbers(LottoTicket(winnings), bonus)
    }

    private fun makeRandomNumbers(
        count: Int = TICKET_SIZE,
        filter: List<Int> = emptyList(),
    ): List<Int> = (MIN_NUMBER..MAX_NUMBER).filter { it !in filter }.shuffled().take(count)
}
