package lotto.model

enum class LottoRank(
    val prize: Int,
) {
    FIRST(2_100_000_000),
    SECOND(60_000_000),
    THIRD(1_500_000),
    FOURTH(50_000),
    FIFTH(5_000),
    LOSE(0),
}
