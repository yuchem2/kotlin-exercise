package lotto.model

import kotlinx.serialization.Serializable

@Serializable
class LottoTickets(
    private val tickets: List<LottoTicket>,
) {
    val size: Int get() = tickets.size

    operator fun plus(other: LottoTickets) = LottoTickets(tickets + other.tickets)

    fun groupByRank(winning: WinningNumbers): Map<LottoRank, Int> = tickets.groupBy { winning.rankOf(it) }.mapValues { it.value.size }
}
