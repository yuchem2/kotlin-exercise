package lotto.model

import kotlinx.serialization.Serializable

@Serializable
class WinningNumbers(
    private val mainNumbers: LottoNumbers,
    private val bonus: LottoNumber,
) {
    init {
        mainNumbers.requireFull()
        require(bonus !in mainNumbers) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
    }

    fun rankOf(ticket: LottoTicket): LottoRank = LottoRank.of(countMatch(ticket), containsBonus(ticket))

    private fun countMatch(ticket: LottoTicket): Int = mainNumbers.count(ticket.numbers)

    private fun containsBonus(ticket: LottoTicket): Boolean = bonus in ticket
}
