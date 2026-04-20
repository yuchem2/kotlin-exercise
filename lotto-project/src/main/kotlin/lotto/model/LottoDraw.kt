package lotto.model

import lotto.constant.BONUS_COUNT
import lotto.constant.TICKET_SIZE

class LottoDraw(
    val round: Int,
    val tickets: List<LottoTicket>,
    val winningNumbers: LottoTicket,
    val bonusNumbers: List<Int>,
) {
    private val result: MutableMap<LottoRank, Int> = mutableMapOf()

    init {
        require(bonusNumbers.size == BONUS_COUNT) { "보너스 숫자는 ${BONUS_COUNT}개여야 합니다." }
        require(!winningNumbers.numbers.toSet().containsAll(bonusNumbers)) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
        setResult()
    }

    private fun getRank(
        matchNumbers: Int,
        matchBonusNumbers: Int,
    ): LottoRank =
        when (matchNumbers) {
            TICKET_SIZE -> LottoRank.FIRST
            TICKET_SIZE - 1 -> if (matchBonusNumbers == BONUS_COUNT) LottoRank.SECOND else LottoRank.THIRD
            TICKET_SIZE - 2 -> LottoRank.FOURTH
            TICKET_SIZE - 3 -> LottoRank.FIFTH
            else -> LottoRank.LOSE
        }

    private fun setResult() {
        tickets.forEach {
            val matchNumbers = it.compare(winningNumbers)
            val matchBonusNumbers = it.hasBonusNumbers(bonusNumbers)
            val rank = getRank(matchNumbers, matchBonusNumbers)
            result[rank] = result.getOrDefault(rank, 0) + 1
        }
    }

    fun getResult(): Map<LottoRank, Int> = result
}
