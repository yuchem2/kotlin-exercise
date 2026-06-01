package lotto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.builtins.serializer
import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.model.LottoDraws
import lotto.repository.AccountRepository
import lotto.repository.JsonFileStorage
import lotto.repository.LottoDrawRepository
import lotto.service.AccountService
import lotto.service.LottoService
import lotto.support.captureStdout
import lotto.support.withStdin
import lotto.view.InputView
import lotto.view.Menu
import lotto.view.OutputView
import java.io.File

private fun newApp(dir: File): App {
    val accountRepository =
        AccountRepository(JsonFileStorage(File(dir, "account.json").absolutePath, Long.serializer(), default = { 0L }))
    val drawRepository =
        LottoDrawRepository(JsonFileStorage(File(dir, "lotto.json").absolutePath, LottoDraws.serializer(), default = { LottoDraws() }))
    val accountService = AccountService(accountRepository)
    val lottoService = LottoService(accountService, drawRepository)
    val inputView = InputView()
    val outputView = OutputView()

    return App(
        inputView,
        outputView,
        accountHandler = AccountHandler(accountService, outputView),
        depositHandler = DepositHandler(accountService, inputView, outputView),
        purchaseHandler = PurchaseHandler(lottoService, inputView, outputView),
        drawHandler = DrawHandler(lottoService, outputView),
        historyHandler = HistoryHandler(lottoService, inputView, outputView),
    )
}

/** newApp이 쓰는 것과 동일한 저장소 경로에서 현재 잔액을 직접 읽는다(출력 메시지에 의존하지 않는 상태 검증용). */
private fun savedAmount(dir: File): Long =
    AccountRepository(JsonFileStorage(File(dir, "account.json").absolutePath, Long.serializer(), default = { 0L })).load().amount

private data class MenuCase(
    val menu: Menu,
    val input: String,
    val expectedOutput: String,
)

/** 각 메뉴를 handleMenu로 호출하면 해당 핸들러만 내는 고유 출력이 나오는지로 위임을 확인한다. */
private val MENU_CASES =
    listOf(
        MenuCase(Menu.ACCOUNT, input = "", expectedOutput = "현재 잔액"),
        MenuCase(Menu.DEPOSIT, input = "1000\n", expectedOutput = "입금할 금액을 입력하세요"),
        MenuCase(Menu.PURCHASE, input = "1\n0\n0\n", expectedOutput = "구매할 티켓의 양을 입력하세요"),
        MenuCase(Menu.DRAW, input = "", expectedOutput = "진행되고 있는 회차가 없거나, 이미 종료되었습니다."),
        MenuCase(Menu.HISTORY, input = "0\n", expectedOutput = "조회할 회차를 입력하세요"),
    )

class AppTest :
    WordSpec({
        "App.handleMenu" should {
            MENU_CASES.forEach { (menu, input, expectedOutput) ->
                "$menu 메뉴를 해당 핸들러로 위임한다" {
                    val app = newApp(tempdir())

                    val output = withStdin(input) { captureStdout { app.handleMenu(menu) } }

                    output shouldContain expectedOutput
                }
            }
            "EXIT는 도달 불가 상태이므로 예외를 던진다" {
                val app = newApp(tempdir())
                shouldThrow<IllegalStateException> { app.handleMenu(Menu.EXIT) }
            }
        }

        "App.run" should {
            "EXIT까지 메뉴를 처리하고 EXIT 이후 입력은 처리하지 않는다" {
                val dir = tempdir()
                val app = newApp(dir)

                val scenario =
                    "${Menu.DEPOSIT.number}\n1000\n" +
                        "${Menu.EXIT.number}\n" +
                        "${Menu.DEPOSIT.number}\n9999\n" +
                        "${Menu.EXIT.number}\n"

                withStdin(scenario) { captureStdout { app.run() } }

                // EXIT 전 입금만 반영되어야 한다.
                savedAmount(dir) shouldBe 1000L
            }
            "잘못된 입력이 들어오면 에러를 출력하고 계속한다" {
                val app = newApp(tempdir())
                val scenario = "잘못된입력\n${Menu.EXIT.number}\n"

                val output = withStdin(scenario) { captureStdout { app.run() } }

                output shouldContain "[ERROR]"
            }
        }
    })
