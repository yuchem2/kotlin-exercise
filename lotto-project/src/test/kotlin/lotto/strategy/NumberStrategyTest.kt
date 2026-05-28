package lotto.strategy

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import lotto.model.LottoNumber

private fun nums(vararg values: Int) = values.map { LottoNumber(it) }

class NumberStrategyTest :
    WordSpec({
        "수동(manual) 전략" should {
            "입력한 번호를 그대로 반환한다" {
                val numbers = nums(1, 2, 3, 4, 5, 6)
                NumberStrategy.manual(numbers).pick() shouldBe numbers
            }
        }

        "자동(auto) 전략" should {
            "중복 없는 6개 번호를 생성한다" {
                val picked = NumberStrategy.auto().pick()
                picked.size shouldBe 6
                picked.toSet().size shouldBe 6
            }
        }

        "반자동(semiAuto) 전략" should {
            "고정 번호를 포함한 6개를 생성한다" {
                val fixed = nums(1, 2, 3)
                val picked = NumberStrategy.semiAuto(fixed).pick()
                picked.size shouldBe 6
                picked.toSet().size shouldBe 6
                picked shouldContainAll fixed
            }
            "고정 번호가 없으면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { NumberStrategy.semiAuto(emptyList()) }
            }
            "고정 번호가 6개 이상이면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { NumberStrategy.semiAuto(nums(1, 2, 3, 4, 5, 6)) }
            }
        }
    })
