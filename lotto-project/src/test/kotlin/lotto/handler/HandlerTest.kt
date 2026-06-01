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

private class Fixture {
    val accountService = mockk<AccountService>(relaxed = true)
    val lottoService = mockk<LottoService>(relaxed = true)
    val inputView = mockk<InputView>(relaxed = true)
    val outputView = mockk<OutputView>(relaxed = true)

    val accountHandler get() = AccountHandler(accountService, outputView)
    val depositHandler get() = DepositHandler(accountService, inputView, outputView)
    val drawHandler get() = DrawHandler(lottoService, outputView)
    val historyHandler get() = HistoryHandler(lottoService, inputView, outputView)
    val purchaseHandler get() = PurchaseHandler(lottoService, inputView, outputView)
}

class AccountHandlerTest :
    WordSpec({
        "AccountHandler.handle" should {
            "현재 잔액을 출력한다" {
                val f = Fixture()
                every { f.accountService.getAmount() } returns 5000

                f.accountHandler.handle()

                verify(exactly = 1) { f.outputView.printBalance(5000) }
            }
        }
    })

class DepositHandlerTest :
    WordSpec({
        "DepositHandler.handle" should {
            "입력 금액을 입금하고 잔액을 출력한다" {
                val f = Fixture()
                every { f.inputView.inputPositive() } returns 1000

                f.depositHandler.handle()

                verify(exactly = 1) { f.accountService.deposit(1000L) }
                verify(exactly = 1) { f.outputView.printBalance(any()) }
            }
            "입력이 잘못되면 에러를 출력한다" {
                val f = Fixture()
                every { f.inputView.inputPositive() } throws IllegalArgumentException("잘못된 입력")

                f.depositHandler.handle()

                verify(exactly = 1) { f.outputView.printError("잘못된 입력") }
            }
        }
    })

class DrawHandlerTest :
    WordSpec({
        "DrawHandler.handle" should {
            "진행 중인 회차가 없으면 안내 메시지를 출력한다" {
                val f = Fixture()
                every { f.lottoService.endAndSave() } returns null

                f.drawHandler.handle()

                verify(exactly = 1) { f.outputView.printMessage("진행되고 있는 회차가 없거나, 이미 종료되었습니다.") }
            }
            "종료된 회차의 요약을 출력한다" {
                val f = Fixture()
                val draw = mockk<LottoDraw> { every { getSummary() } returns "요약결과" }
                every { f.lottoService.endAndSave() } returns draw

                f.drawHandler.handle()

                verify(exactly = 1) { f.outputView.printMessage("요약결과") }
            }
        }
    })

class HistoryHandlerTest :
    WordSpec({
        "HistoryHandler.handle 전체 조회(0)" should {
            "이력이 있으면 표를 출력한다" {
                val f = Fixture()
                every { f.lottoService.getAllHistory() } returns draws(1)
                every { f.inputView.inputNonNegative() } returns 0

                f.historyHandler.handle()

                verify(exactly = 1) { f.outputView.printHistory(any()) }
            }
            "이력이 없으면 안내 메시지를 출력한다" {
                val f = Fixture()
                every { f.lottoService.getAllHistory() } returns LottoDraws()
                every { f.inputView.inputNonNegative() } returns 0

                f.historyHandler.handle()

                verify(exactly = 1) { f.outputView.printMessage("진행된 회차가 없습니다.") }
            }
        }

        "HistoryHandler.handle 회차 조회" should {
            "회차를 찾으면 요약을 출력한다" {
                val f = Fixture()
                val draw = mockk<LottoDraw> { every { getSummary() } returns "1회차요약" }
                every { f.lottoService.getHistoryByRound(1) } returns draw
                every { f.inputView.inputNonNegative() } returns 1

                f.historyHandler.handle()

                verify(exactly = 1) { f.outputView.printMessage("1회차요약") }
            }
            "회차가 없으면 에러를 출력한다" {
                val f = Fixture()
                every { f.lottoService.getHistoryByRound(99) } returns null
                every { f.inputView.inputNonNegative() } returns 99

                f.historyHandler.handle()

                verify(exactly = 1) { f.outputView.printError("잘못된 회차 번호입니다.") }
            }
            "입력이 잘못되면 에러를 출력한다" {
                val f = Fixture()
                every { f.inputView.inputNonNegative() } throws IllegalArgumentException("잘못된 입력")

                f.historyHandler.handle()

                verify(exactly = 1) { f.outputView.printError("잘못된 입력") }
            }
        }
    })

class PurchaseHandlerTest :
    WordSpec({
        "PurchaseHandler.handle" should {
            "자동 구매를 처리하고 완료 메시지를 출력한다" {
                val f = Fixture()
                every { f.inputView.inputPositive() } returns 3
                every { f.inputView.inputNonNegative() } returnsMany listOf(0, 0)

                f.purchaseHandler.handle()

                verify(exactly = 1) { f.lottoService.purchaseAndSave(any()) }
                verify(exactly = 1) { f.outputView.printMessage("구매가 완료되었습니다.") }
            }
            "수동/반자동 입력을 처리한다" {
                val f = Fixture()
                every { f.inputView.inputPositive() } returns 2
                every { f.inputView.inputNonNegative() } returnsMany listOf(1, 1)
                every { f.inputView.inputTicketNumbers() } returnsMany
                    listOf(
                        (1..6).map { LottoNumber(it) },
                        (7..9).map { LottoNumber(it) },
                    )

                f.purchaseHandler.handle()

                verify(exactly = 1) { f.lottoService.purchaseAndSave(any()) }
            }
            "수동 수량이 전체보다 많으면 에러를 출력한다" {
                val f = Fixture()
                every { f.inputView.inputPositive() } returns 1
                every { f.inputView.inputNonNegative() } returns 2

                f.purchaseHandler.handle()

                verify(exactly = 1) { f.outputView.printError(any()) }
            }
        }
    })
