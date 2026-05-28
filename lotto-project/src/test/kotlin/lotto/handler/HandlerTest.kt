package lotto.handler

import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.model.LottoTickets
import lotto.service.AccountService
import lotto.service.LottoService
import lotto.view.InputView
import lotto.view.OutputView

private fun numbers(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

private fun draws(round: Int) = LottoDraws().append(LottoDraw(round = round, tickets = LottoTickets(listOf(numbers(1, 2, 3, 4, 5, 6)))))

class AccountHandlerTest :
    WordSpec({
        "AccountHandler.handle" should {
            "현재 잔액을 출력한다" {
                val accountService = mockk<AccountService> { every { getAmount() } returns 5000 }
                val outputView = mockk<OutputView>(relaxed = true)

                AccountHandler(accountService, outputView).handle()

                verify { outputView.printBalance(5000) }
            }
        }
    })

class DepositHandlerTest :
    WordSpec({
        "DepositHandler.handle" should {
            "입력 금액을 입금하고 잔액을 출력한다" {
                val accountService = mockk<AccountService>(relaxed = true)
                val inputView = mockk<InputView> { every { inputPositive() } returns 1000 }
                val outputView = mockk<OutputView>(relaxed = true)

                DepositHandler(accountService, inputView, outputView).handle()

                verify { accountService.deposit(1000L) }
                verify { outputView.printBalance(any()) }
            }
            "입력이 잘못되면 에러를 출력한다" {
                val inputView = mockk<InputView> { every { inputPositive() } throws IllegalArgumentException("잘못된 입력") }
                val outputView = mockk<OutputView>(relaxed = true)

                DepositHandler(mockk(relaxed = true), inputView, outputView).handle()

                verify { outputView.printError("잘못된 입력") }
            }
        }
    })

class DrawHandlerTest :
    WordSpec({
        "DrawHandler.handle" should {
            "진행 중인 회차가 없으면 안내 메시지를 출력한다" {
                val lottoService = mockk<LottoService> { every { endAndSave() } returns null }
                val outputView = mockk<OutputView>(relaxed = true)

                DrawHandler(lottoService, outputView).handle()

                verify { outputView.printMessage("진행되고 있는 회차가 없거나, 이미 종료되었습니다.") }
            }
            "종료된 회차의 요약을 출력한다" {
                val draw = mockk<LottoDraw> { every { getSummary() } returns "요약결과" }
                val lottoService = mockk<LottoService> { every { endAndSave() } returns draw }
                val outputView = mockk<OutputView>(relaxed = true)

                DrawHandler(lottoService, outputView).handle()

                verify { outputView.printMessage("요약결과") }
            }
        }
    })

class HistoryHandlerTest :
    WordSpec({
        "HistoryHandler.handle 전체 조회(0)" should {
            "이력이 있으면 표를 출력한다" {
                val lottoService = mockk<LottoService> { every { getAllHistory() } returns draws(1) }
                val inputView = mockk<InputView> { every { inputNonNegative() } returns 0 }
                val outputView = mockk<OutputView>(relaxed = true)

                HistoryHandler(lottoService, inputView, outputView).handle()

                verify { outputView.printHistory(any()) }
            }
            "이력이 없으면 안내 메시지를 출력한다" {
                val lottoService = mockk<LottoService> { every { getAllHistory() } returns LottoDraws() }
                val inputView = mockk<InputView> { every { inputNonNegative() } returns 0 }
                val outputView = mockk<OutputView>(relaxed = true)

                HistoryHandler(lottoService, inputView, outputView).handle()

                verify { outputView.printMessage("진행된 회차가 없습니다.") }
            }
        }

        "HistoryHandler.handle 회차 조회" should {
            "회차를 찾으면 요약을 출력한다" {
                val draw = mockk<LottoDraw> { every { getSummary() } returns "1회차요약" }
                val lottoService = mockk<LottoService> { every { getHistoryByRound(1) } returns draw }
                val inputView = mockk<InputView> { every { inputNonNegative() } returns 1 }
                val outputView = mockk<OutputView>(relaxed = true)

                HistoryHandler(lottoService, inputView, outputView).handle()

                verify { outputView.printMessage("1회차요약") }
            }
            "회차가 없으면 에러를 출력한다" {
                val lottoService = mockk<LottoService> { every { getHistoryByRound(99) } returns null }
                val inputView = mockk<InputView> { every { inputNonNegative() } returns 99 }
                val outputView = mockk<OutputView>(relaxed = true)

                HistoryHandler(lottoService, inputView, outputView).handle()

                verify { outputView.printError("잘못된 회차 번호입니다.") }
            }
            "입력이 잘못되면 에러를 출력한다" {
                val inputView = mockk<InputView> { every { inputNonNegative() } throws IllegalArgumentException("잘못된 입력") }
                val outputView = mockk<OutputView>(relaxed = true)

                HistoryHandler(mockk(relaxed = true), inputView, outputView).handle()

                verify { outputView.printError("잘못된 입력") }
            }
        }
    })

class PurchaseHandlerTest :
    WordSpec({
        "PurchaseHandler.handle" should {
            "자동 구매를 처리하고 완료 메시지를 출력한다" {
                val lottoService = mockk<LottoService>(relaxed = true)
                val inputView =
                    mockk<InputView> {
                        every { inputPositive() } returns 3
                        every { inputNonNegative() } returnsMany listOf(0, 0)
                    }
                val outputView = mockk<OutputView>(relaxed = true)

                PurchaseHandler(lottoService, inputView, outputView).handle()

                verify { lottoService.purchaseAndSave(any()) }
                verify { outputView.printMessage("구매가 완료되었습니다.") }
            }
            "수동/반자동 입력을 처리한다" {
                val lottoService = mockk<LottoService>(relaxed = true)
                val inputView =
                    mockk<InputView> {
                        every { inputPositive() } returns 2
                        every { inputNonNegative() } returnsMany listOf(1, 1)
                        every { inputTicketNumbers() } returnsMany
                            listOf(
                                (1..6).map { LottoNumber(it) },
                                (7..9).map { LottoNumber(it) },
                            )
                    }
                val outputView = mockk<OutputView>(relaxed = true)

                PurchaseHandler(lottoService, inputView, outputView).handle()

                verify { lottoService.purchaseAndSave(any()) }
            }
            "수동 수량이 전체보다 많으면 에러를 출력한다" {
                val inputView =
                    mockk<InputView> {
                        every { inputPositive() } returns 1
                        every { inputNonNegative() } returns 2
                    }
                val outputView = mockk<OutputView>(relaxed = true)

                PurchaseHandler(mockk(relaxed = true), inputView, outputView).handle()

                verify { outputView.printError(any()) }
            }
        }
    })
