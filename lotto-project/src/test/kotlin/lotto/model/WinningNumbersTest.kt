package lotto.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class WinningNumbersTest :
    WordSpec({
        "WinningNumbers 생성" should {
            "보너스가 당첨번호에 없으면 생성된다" {
                val main = LottoNumbers((1..6).map { LottoNumber(it) })
                val winning = WinningNumbers(main, LottoNumber(7))
                winning.bonus shouldBe LottoNumber(7)
            }
            "보너스가 당첨번호에 포함되면 예외를 던진다" {
                val main = LottoNumbers((1..6).map { LottoNumber(it) })
                shouldThrow<IllegalArgumentException> { WinningNumbers(main, LottoNumber(6)) }
            }
        }
    })
