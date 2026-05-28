package lotto.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

private fun numbersOf(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

class LottoNumberTest :
    WordSpec({
        "LottoNumber 생성" should {
            "경계값 1과 45는 허용한다" {
                LottoNumber(1).number shouldBe 1
                LottoNumber(45).number shouldBe 45
            }
            "0 이하는 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { LottoNumber(0) }
            }
            "45를 초과하면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { LottoNumber(46) }
            }
        }

        "LottoNumber.ALL_NUMBERS" should {
            "1부터 45까지 45개를 가진다" {
                LottoNumber.ALL_NUMBERS.size shouldBe 45
                LottoNumber.ALL_NUMBERS.first() shouldBe LottoNumber(1)
                LottoNumber.ALL_NUMBERS.last() shouldBe LottoNumber(45)
            }
        }

        "LottoNumbers 생성" should {
            "서로 다른 6개면 생성된다" {
                numbersOf(1, 2, 3, 4, 5, 6).size shouldBe 6
            }
            "중복이 있으면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { numbersOf(1, 1, 2, 3, 4, 5) }
            }
            "6개가 아니면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { numbersOf(1, 2, 3, 4, 5) }
                shouldThrow<IllegalArgumentException> { numbersOf(1, 2, 3, 4, 5, 6, 7) }
            }
        }

        "LottoNumbers.count" should {
            "다른 묶음과 겹치는 번호 개수를 센다" {
                val a = numbersOf(1, 2, 3, 4, 5, 6)
                val b = numbersOf(4, 5, 6, 7, 8, 9)
                a.count(b) shouldBe 3
            }
            "겹치는 번호가 없으면 0이다" {
                numbersOf(1, 2, 3, 4, 5, 6).count(numbersOf(7, 8, 9, 10, 11, 12)) shouldBe 0
            }
        }

        "LottoNumbers.contains" should {
            "포함된 번호는 true, 아니면 false를 반환한다" {
                val numbers = numbersOf(1, 2, 3, 4, 5, 6)
                (LottoNumber(3) in numbers) shouldBe true
                (LottoNumber(7) in numbers) shouldBe false
            }
        }

        "LottoNumbers.toString" should {
            "각 번호를 쉼표로 구분해 이어붙인다" {
                numbersOf(1, 2, 3, 4, 5, 6).toString() shouldBe
                    "LottoNumber(number=1), LottoNumber(number=2), LottoNumber(number=3), " +
                    "LottoNumber(number=4), LottoNumber(number=5), LottoNumber(number=6)"
            }
        }

        "LottoNumbers.plus" should {
            "두 묶음을 합치면 6개를 초과해 예외를 던진다" {
                shouldThrow<IllegalArgumentException> {
                    numbersOf(1, 2, 3, 4, 5, 6) + numbersOf(7, 8, 9, 10, 11, 12)
                }
            }
        }
    })
