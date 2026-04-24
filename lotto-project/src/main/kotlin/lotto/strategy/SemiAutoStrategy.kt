package lotto.strategy

import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.LottoTicket
import lotto.service.RandomNumberGenerator

class SemiAutoStrategy(
    private val fixed: List<Int>,
) : TicketStrategy {
    init {
        require(fixed.size < TICKET_SIZE) { "반자동 번호는 ${TICKET_SIZE}개 미만이어야 합니다." }
        require(fixed.isNotEmpty()) { "반자동 번호는 1개 이상이어야 합니다." }
        require(fixed.all { it in MIN_NUMBER..MAX_NUMBER }) { "로또 번호는 ${MIN_NUMBER}와 $MAX_NUMBER 사이여야 합니다" }
    }

    override fun create(): LottoTicket {
        val remaining = RandomNumberGenerator.pick(TICKET_SIZE - fixed.size, fixed)
        return LottoTicket(fixed + remaining)
    }
}
