package lotto.model

import kotlinx.serialization.Serializable
import lotto.util.padEndKo
import lotto.util.toFormattedString

data class LottoResult(
    val result: Map<LottoRank, Int>,
    val totalIncome: Long,
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
    private var tickets: LottoTickets,
    private var winningNumbers: WinningNumbers? = null,
    private var result: Map<LottoRank, Int> = emptyMap(),
    private var totalIncome: Long = 0,
    private var isEnded: Boolean = false,
    private var finalTicketCount: Int = 0,
) {
    constructor(round: Int, tickets: LottoTickets) : this(
        round = round,
        tickets = tickets,
        finalTicketCount = tickets.size,
    )

    fun setResult() {
        if (isEnded) return

        result = tickets.groupByRank(winningNumbers ?: return)
        totalIncome = result.entries.fold(0) { acc, (rank, count) -> acc + rank.prize * count }
        this.tickets = LottoTickets()
        isEnded = true
    }

    fun addTicket(tickets: LottoTickets) {
        if (isEnded) return
        this.tickets += tickets
        finalTicketCount = this.tickets.size
    }

    fun endDraw(winningNumbers: WinningNumbers) {
        this.winningNumbers = winningNumbers
        setResult()
    }

    fun isEnded() = isEnded

    fun getResult(): LottoResult = LottoResult(result, totalIncome)

    override fun toString(): String =
        if (isEnded) {
            "#$round".padEndKo(6) + finalTicketCount.toFormattedString().padEndKo(8) + getResult().toString()
        } else {
            "#$round".padEndKo(6) + "진행 중".padEndKo(8)
        }

    fun getSummary(): String =
        if (isEnded) {
            buildString {
                appendLine("회차: $round")
                appendLine("총 티켓: ${finalTicketCount.toFormattedString()}")
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
                appendLine("총 티켓: ${finalTicketCount.toFormattedString()}")
                appendLine("아직 진행 중인 회차입니다.")
            }
        }
}
