package lotto.service

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lotto.model.Account
import lotto.repository.AccountRepository

class AccountServiceTest :
    WordSpec({
        "AccountService.getAmount" should {
            "계좌 잔액을 반환한다" {
                val repository = mockk<AccountRepository>()
                every { repository.load() } returns Account(1000)

                AccountService(repository).getAmount() shouldBe 1000L
            }
        }

        "AccountService.deposit" should {
            "입금한 뒤 계좌를 저장한다" {
                val repository = mockk<AccountRepository>(relaxed = true)
                val account = Account(1000)
                every { repository.load() } returns account

                AccountService(repository).deposit(500)

                account.amount shouldBe 1500
                verify { repository.save(account) }
            }
        }

        "AccountService.withdraw" should {
            "출금한 뒤 계좌를 저장한다" {
                val repository = mockk<AccountRepository>(relaxed = true)
                val account = Account(1000)
                every { repository.load() } returns account

                AccountService(repository).withdraw(400)

                account.amount shouldBe 600
                verify { repository.save(account) }
            }
        }
    })
