package lotto.service

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import lotto.constant.TICKET_PRICE
import lotto.generator.WinningNumberGenerator
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.model.LottoRank
import lotto.model.LottoTickets
import lotto.model.WinningNumbers
import lotto.repository.LottoDrawRepository
import lotto.strategy.NumberStrategy

private fun numbers(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

private fun manualStrategy(vararg values: Int) = NumberStrategy.manual(values.map { LottoNumber(it) })

private fun inProgressDraw(round: Int) = LottoDraw(round = round, tickets = LottoTickets(listOf(numbers(1, 2, 3, 4, 5, 6))))

class LottoServiceTest :
    WordSpec({
        "LottoService.purchaseAndSave" should {
            "진행 중인 회차가 없으면 새 회차를 생성하고 차감/저장한다" {
                val accountService = mockk<AccountService>(relaxed = true)
                val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                every { drawRepository.load() } returns LottoDraws()

                val tickets = LottoService(accountService, drawRepository).purchaseAndSave(listOf(manualStrategy(1, 2, 3, 4, 5, 6)))

                tickets.size shouldBe 1
                verify { accountService.withdraw(1 * TICKET_PRICE) }
                verify { drawRepository.save(any()) }
            }

            "진행 중인 회차가 있으면 티켓을 추가한다" {
                val accountService = mockk<AccountService>(relaxed = true)
                val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                every { drawRepository.load() } returns LottoDraws().append(inProgressDraw(1))

                val tickets =
                    LottoService(accountService, drawRepository)
                        .purchaseAndSave(listOf(manualStrategy(7, 8, 9, 10, 11, 12), manualStrategy(13, 14, 15, 16, 17, 18)))

                tickets.size shouldBe 2
                verify { accountService.withdraw(2 * TICKET_PRICE) }
            }
        }

        "LottoService.endAndSave" should {
            "진행 중인 회차가 없으면 null을 반환한다" {
                val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                every { drawRepository.load() } returns LottoDraws()

                LottoService(mockk(relaxed = true), drawRepository).endAndSave().shouldBeNull()
            }

            "이미 종료된 회차면 null을 반환한다" {
                val ended = inProgressDraw(1).apply { endDraw(WinningNumbers(numbers(10, 11, 12, 13, 14, 15), LottoNumber(20))) }
                val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                every { drawRepository.load() } returns LottoDraws().append(ended)

                LottoService(mockk(relaxed = true), drawRepository).endAndSave().shouldBeNull()
            }

            "당첨되면 회차를 종료하고 수익을 입금한다" {
                mockkObject(WinningNumberGenerator)
                try {
                    every { WinningNumberGenerator.generate() } returns WinningNumbers(numbers(1, 2, 3, 4, 5, 6), LottoNumber(7))
                    val accountService = mockk<AccountService>(relaxed = true)
                    val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                    every { drawRepository.load() } returns LottoDraws().append(inProgressDraw(1))

                    val result = LottoService(accountService, drawRepository).endAndSave()

                    result.shouldNotBeNull()
                    result.isEnded() shouldBe true
                    verify { accountService.deposit(LottoRank.FIRST.prize) }
                } finally {
                    unmockkObject(WinningNumberGenerator)
                }
            }

            "당첨이 없으면 입금하지 않는다" {
                mockkObject(WinningNumberGenerator)
                try {
                    every { WinningNumberGenerator.generate() } returns WinningNumbers(numbers(20, 21, 22, 23, 24, 25), LottoNumber(30))
                    val accountService = mockk<AccountService>(relaxed = true)
                    val drawRepository = mockk<LottoDrawRepository>(relaxed = true)
                    every { drawRepository.load() } returns LottoDraws().append(inProgressDraw(1))

                    LottoService(accountService, drawRepository).endAndSave()

                    verify(exactly = 0) { accountService.deposit(any()) }
                } finally {
                    unmockkObject(WinningNumberGenerator)
                }
            }
        }

        "LottoService 조회" should {
            "전체 이력을 반환한다" {
                val drawRepository = mockk<LottoDrawRepository>()
                val draws = LottoDraws().append(inProgressDraw(1))
                every { drawRepository.load() } returns draws

                LottoService(mockk(relaxed = true), drawRepository).getAllHistory() shouldBe draws
            }

            "회차 번호로 조회한다" {
                val drawRepository = mockk<LottoDrawRepository>()
                every { drawRepository.load() } returns LottoDraws().append(inProgressDraw(1))

                LottoService(mockk(relaxed = true), drawRepository).getHistoryByRound(1)?.round shouldBe 1
            }
        }
    })
