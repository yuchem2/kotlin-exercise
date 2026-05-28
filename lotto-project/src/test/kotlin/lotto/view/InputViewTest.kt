package lotto.view

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import lotto.model.LottoNumber
import lotto.support.withStdin

class InputViewTest :
    WordSpec({
        val view = InputView()

        "InputView.inputNumber" should {
            "숫자를 입력하면 Int로 반환한다" {
                withStdin("5\n") { view.inputNumber() } shouldBe 5
            }
            "숫자가 아니면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { withStdin("abc\n") { view.inputNumber() } }
            }
            "입력이 없으면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { withStdin("") { view.inputNumber() } }
            }
        }

        "InputView.inputNonNegative" should {
            "0 이상이면 반환한다" {
                withStdin("0\n") { view.inputNonNegative() } shouldBe 0
            }
            "음수면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { withStdin("-1\n") { view.inputNonNegative() } }
            }
        }

        "InputView.inputPositive" should {
            "양수면 반환한다" {
                withStdin("3\n") { view.inputPositive() } shouldBe 3
            }
            "0 이하면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { withStdin("0\n") { view.inputPositive() } }
            }
        }

        "InputView.inputMenu" should {
            "번호에 해당하는 메뉴를 반환한다" {
                withStdin("1\n") { view.inputMenu() } shouldBe Menu.DEPOSIT
            }
        }

        "InputView.inputTicketNumbers" should {
            "공백으로 구분된 번호를 파싱한다" {
                withStdin("1 2 3 4 5 6\n") { view.inputTicketNumbers() } shouldBe (1..6).map { LottoNumber(it) }
            }
            "숫자가 아닌 토큰이 있으면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { withStdin("1 a 3\n") { view.inputTicketNumbers() } }
            }
        }
    })
