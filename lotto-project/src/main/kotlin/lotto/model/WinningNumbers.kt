package lotto.model

import kotlinx.serialization.Serializable

@Serializable
class WinningNumbers(
    private val mainNumbers: LottoNumbers,
    private val bonus: LottoNumber,
) {
    init {
        require(bonus !in mainNumbers) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
    }

    fun rankOf(ticket: LottoNumbers): LottoRank = LottoRank.of(countMatch(ticket), containsBonus(ticket))

    private fun countMatch(ticket: LottoNumbers): Int = mainNumbers.count(ticket)

    private fun containsBonus(ticket: LottoNumbers): Boolean = bonus in ticket
}
