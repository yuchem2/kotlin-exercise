package lotto.model

import kotlinx.serialization.Serializable
import lotto.constant.BONUS_COUNT
import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE

@Serializable
class WinningNumbers(
    private val mainNumbers: LottoTicket,
    private val bonus: List<Int>,
) {
    init {
        require(bonus.size == BONUS_COUNT) { "보너스 숫자는 ${BONUS_COUNT}개여야 합니다." }
        require(bonus.all { it in MIN_NUMBER..MAX_NUMBER }) { "보너스는 $MIN_NUMBER~$MAX_NUMBER 사이여야 합니다." }
        require(mainNumbers.countBonusMatches(bonus) == 0) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
    }

    fun rankOf(ticket: LottoTicket): LottoRank {
        val match = ticket.countMatch(mainNumbers)
        val bonusMatch = ticket.countBonusMatches(bonus)
        return when (match) {
            TICKET_SIZE -> LottoRank.FIRST
            TICKET_SIZE - 1 -> if (bonusMatch == BONUS_COUNT) LottoRank.SECOND else LottoRank.THIRD
            TICKET_SIZE - 2 -> LottoRank.FOURTH
            TICKET_SIZE - 3 -> LottoRank.FIFTH
            else -> LottoRank.LOSE
        }
    }
}
