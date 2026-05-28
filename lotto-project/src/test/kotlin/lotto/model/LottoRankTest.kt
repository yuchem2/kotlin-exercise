package lotto.model

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class LottoRankTest :
    WordSpec({
        "LottoRank.of" should {
            "6개 일치하면 FIRST를 반환한다" {
                LottoRank.of(match = 6, bonusMatch = false) shouldBe LottoRank.FIRST
            }
            "5개 일치하고 보너스가 맞으면 SECOND를 반환한다" {
                LottoRank.of(match = 5, bonusMatch = true) shouldBe LottoRank.SECOND
            }
            "5개 일치하고 보너스가 없으면 THIRD를 반환한다" {
                LottoRank.of(match = 5, bonusMatch = false) shouldBe LottoRank.THIRD
            }
            "4개 일치하고 보너스가 없으면 FOURTH를 반환한다" {
                LottoRank.of(match = 4, bonusMatch = false) shouldBe LottoRank.FOURTH
            }
            "3개 일치하면 FIFTH를 반환한다" {
                LottoRank.of(match = 3, bonusMatch = false) shouldBe LottoRank.FIFTH
            }
            "2개 이하로 일치하면 LOSE를 반환한다" {
                LottoRank.of(match = 2, bonusMatch = false) shouldBe LottoRank.LOSE
                LottoRank.of(match = 0, bonusMatch = false) shouldBe LottoRank.LOSE
            }
        }
    })
