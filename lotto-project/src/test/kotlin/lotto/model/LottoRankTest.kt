package lotto.model

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

private data class RankCase(
    val match: Int,
    val bonusMatch: Boolean,
    val expected: LottoRank,
)

class LottoRankTest :
    WordSpec({
        "LottoRank.of" should {
            listOf(
                RankCase(match = 6, bonusMatch = false, expected = LottoRank.FIRST),
                RankCase(match = 5, bonusMatch = true, expected = LottoRank.SECOND),
                RankCase(match = 5, bonusMatch = false, expected = LottoRank.THIRD),
                RankCase(match = 4, bonusMatch = false, expected = LottoRank.FOURTH),
                RankCase(match = 3, bonusMatch = false, expected = LottoRank.FIFTH),
                RankCase(match = 2, bonusMatch = false, expected = LottoRank.LOSE),
                RankCase(match = 0, bonusMatch = false, expected = LottoRank.LOSE),
            ).forEach { (match, bonusMatch, expected) ->
                "${match}개 일치(보너스=$bonusMatch)면 $expected 를 반환한다" {
                    LottoRank.of(match = match, bonusMatch = bonusMatch) shouldBe expected
                }
            }
        }
    })
