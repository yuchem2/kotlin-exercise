package lotto.view

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class MenuTest :
    WordSpec({
        "Menu.from" should {
            "번호에 해당하는 메뉴를 반환한다" {
                Menu.from(1) shouldBe Menu.DEPOSIT
                Menu.from(0) shouldBe Menu.EXIT
            }
            "없는 번호면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Menu.from(99) }
            }
        }
    })
