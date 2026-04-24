package lotto.service

import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE

object RandomNumberGenerator {
    fun pick(
        count: Int = TICKET_SIZE,
        filter: List<Int> = emptyList(),
    ): List<Int> = (MIN_NUMBER..MAX_NUMBER).filter { it !in filter }.shuffled().take(count)
}
