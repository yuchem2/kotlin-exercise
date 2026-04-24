package lotto.model

import kotlinx.serialization.Serializable

@Serializable
class LottoDraws(
    private val draws: List<LottoDraw>,
) {
    fun lastRound(): Int = draws.maxOfOrNull { it.round } ?: 0

    fun findByRound(round: Int): LottoDraw? = draws.find { it.round == round }

    fun isNotEmpty(): Boolean = draws.isNotEmpty()

    fun isEmpty(): Boolean = draws.isEmpty()

    fun last(): LottoDraw? = draws.lastOrNull()

    fun append(draw: LottoDraw) = LottoDraws(draws + draw)

    fun updateLast(draw: LottoDraw): LottoDraws {
        if (draws.isEmpty()) return this
        return LottoDraws(draws.dropLast(1) + draw)
    }

    fun toDisplayString(): String = draws.joinToString("\n")
}
