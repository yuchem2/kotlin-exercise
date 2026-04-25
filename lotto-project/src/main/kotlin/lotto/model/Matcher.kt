package lotto.model

@JvmInline
value class Matcher(
    private val winningNumbers: WinningNumbers,
) {
    fun groupByRank(tickets: LottoTickets): Map<LottoRank, Int> =
        tickets
            .groupBy { rankOf(it) }
            .mapValues { it.value.size }

    private fun rankOf(ticket: LottoNumbers): LottoRank = LottoRank.of(countMatch(ticket), containsBonus(ticket))

    private fun countMatch(ticket: LottoNumbers): Int = winningNumbers.mainNumbers.count(ticket)

    private fun containsBonus(ticket: LottoNumbers): Boolean = winningNumbers.bonus in ticket
}
