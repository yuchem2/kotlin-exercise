package lotto.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class AccountTest :
    WordSpec({
        "Account 생성" should {
            "0 이상이면 생성된다" {
                Account(0).amount shouldBe 0
                Account(1000).amount shouldBe 1000
            }
            "음수면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Account(-1) }
            }
        }

        "Account.deposit" should {
            "잔액을 늘린다" {
                val account = Account(1000)
                account.deposit(500)
                account.amount shouldBe 1500
            }
            "0 이하 입금은 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Account(1000).deposit(0) }
            }
        }

        "Account.withdraw" should {
            "잔액을 줄인다" {
                val account = Account(1000)
                account.withdraw(400)
                account.amount shouldBe 600
            }
            "0 이하 출금은 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Account(1000).withdraw(0) }
            }
            "잔액보다 많이 출금하면 예외를 던진다" {
                shouldThrow<IllegalArgumentException> { Account(1000).withdraw(1001) }
            }
        }
    })
