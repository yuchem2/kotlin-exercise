package lotto.view

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class MenuTest :
    WordSpec({
        "Menu.from" should {
            Menu.entries.forEach { menu ->
                "${menu.number}번은 $menu 를 반환한다" {
                    Menu.from(menu.number) shouldBe menu
                }
            }
            "없는 번호면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Menu.from(99) }
            }
        }
    })
