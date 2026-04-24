package lotto.model

enum class LottoRank(
    val prize: Long,
) {
    FIRST(2_100_000_000L),
    SECOND(60_000_000L),
    THIRD(1_500_000L),
    FOURTH(50_000L),
    FIFTH(5_000L),
    LOSE(0L),
}
