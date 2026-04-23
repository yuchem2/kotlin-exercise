package lotto.model

import kotlinx.serialization.Serializable
import lotto.constant.BONUS_COUNT
import lotto.constant.TICKET_SIZE
import lotto.util.padEndKo
import lotto.util.toFormattedString

data class LottoResult(
    val result: Map<LottoRank, Int>,
    val totalIncome: Int,
) {
    override fun toString(): String {
        val str =
            LottoRank.entries.fold("") { acc, rank ->
                "$acc${(result[rank] ?: 0).toFormattedString().padEndKo(13)}"
            }
        return "$str${totalIncome.toFormattedString().padEndKo(19)}"
    }
}

@Serializable
class LottoDraw(
    val round: Int,
    private val tickets: MutableList<LottoTicket>,
    var winningNumbers: LottoTicket? = null,
    var bonusNumbers: List<Int>? = null,
    private val result: MutableMap<LottoRank, Int> = mutableMapOf(),
    private var totalIncome: Int = 0,
    private var isEnded: Boolean = false,
) {
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

    fun setResult() {
        if (isEnded) return

        tickets.forEach {
            val matchNumbers = it.compare(winningNumbers)
            val matchBonusNumbers = it.hasBonusNumbers(bonusNumbers)
            val rank = getRank(matchNumbers, matchBonusNumbers)
            result[rank] = result.getOrDefault(rank, 0) + 1
        }
        totalIncome = result.entries.fold(0) { acc, (rank, count) -> acc + rank.prize * count }
        isEnded = true
    }

    fun addTicket(tickets: List<LottoTicket>) {
        if (isEnded) return
        this.tickets.addAll(tickets)
    }

    fun endDraw(
        winningNumbers: LottoTicket,
        bonusNumbers: List<Int>,
    ) {
        require(bonusNumbers.size == BONUS_COUNT) { "보너스 숫자는 ${BONUS_COUNT}개여야 합니다." }
        require(!winningNumbers.numbers.toSet().containsAll(bonusNumbers)) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
        this.winningNumbers = winningNumbers
        this.bonusNumbers = bonusNumbers
        setResult()
    }

    fun isEnded() = isEnded

    fun getResult(): LottoResult = LottoResult(result, totalIncome)

    override fun toString(): String =
        if (isEnded) {
            "#$round".padEndKo(6) + tickets.size.toFormattedString().padEndKo(8) + getResult().toString()
        } else {
            "#$round".padEndKo(6) + "진행 중".padEndKo(8)
        }

    fun getSummary(): String =
        if (isEnded) {
            buildString {
                appendLine("회차: $round")
                appendLine("총 티켓: ${tickets.size.toFormattedString()}")
                appendLine("─────────────────")
                LottoRank.entries
                    .filter { it != LottoRank.LOSE }
                    .forEach { rank ->
                        appendLine("${rank.ordinal + 1}등: ${result[rank] ?: 0}개 (${rank.prize.toFormattedString()}원)")
                    }
                appendLine("낙첨: ${result[LottoRank.LOSE] ?: 0}개")
                appendLine("─────────────────")
                append("총수익: ${totalIncome.toFormattedString()}원")
            }
        } else {
            buildString {
                appendLine("회차: $round")
                appendLine("총 티켓: ${tickets.size.toFormattedString()}")
                appendLine("아직 진행 중인 회차입니다.")
            }
        }
}
