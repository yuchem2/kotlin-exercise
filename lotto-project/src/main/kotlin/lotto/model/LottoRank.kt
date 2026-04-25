package lotto.model

enum class LottoRank(
    val prize: Long,
    val matchCount: Int,
    val requiredBonus: Boolean = false,
) {
    FIRST(2_100_000_000L, 6),
    SECOND(60_000_000L, 5, true),
    THIRD(1_500_000L, 5),
    FOURTH(50_000L, 4),
    FIFTH(5_000L, 3),
    LOSE(0L, 0),
    ;

    companion object {
        fun of(
            match: Int,
            bonusMatch: Boolean,
        ): LottoRank =
            entries.find { rank ->
                rank.matchCount == match && rank.requiredBonus == bonusMatch
            } ?: LOSE
    }
}
