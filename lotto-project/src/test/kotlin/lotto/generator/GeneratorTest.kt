package lotto.generator

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldNotContainAnyOf
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import lotto.model.LottoNumber

class RandomNumberGeneratorTest :
    WordSpec({
        "RandomNumberGenerator.pick" should {
            "기본값으로 중복 없는 6개를 뽑는다" {
                val picked = RandomNumberGenerator.pick()
                picked.size shouldBe 6
                picked.toSet().size shouldBe 6
                picked.forEach { it.number shouldBeInRange 1..45 }
            }
            "count만큼 뽑는다" {
                RandomNumberGenerator.pick(count = 7).size shouldBe 7
            }
            "filter에 포함된 번호는 제외한다" {
                val filter = (1..40).map { LottoNumber(it) }
                val picked = RandomNumberGenerator.pick(count = 5, filter = filter)
                picked.size shouldBe 5
                picked shouldNotContainAnyOf filter
            }
        }
    })

class WinningNumberGeneratorTest :
    WordSpec({
        "WinningNumberGenerator.generate" should {
            "당첨번호 6개와 보너스를 생성한다" {
                val winning = WinningNumberGenerator.generate()
                winning.mainNumbers.size shouldBe 6
                (winning.bonus in winning.mainNumbers) shouldBe false
            }
        }
    })
