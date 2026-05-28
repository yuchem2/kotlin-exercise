package lotto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.view.InputView
import lotto.view.Menu
import lotto.view.OutputView

private class Fixture {
    val inputView = mockk<InputView>(relaxed = true)
    val outputView = mockk<OutputView>(relaxed = true)
    val accountHandler = mockk<AccountHandler>(relaxed = true)
    val depositHandler = mockk<DepositHandler>(relaxed = true)
    val purchaseHandler = mockk<PurchaseHandler>(relaxed = true)
    val drawHandler = mockk<DrawHandler>(relaxed = true)
    val historyHandler = mockk<HistoryHandler>(relaxed = true)

    val app =
        App(inputView, outputView, accountHandler, depositHandler, purchaseHandler, drawHandler, historyHandler)
}

class AppTest :
    WordSpec({
        "App.handleMenu" should {
            "각 메뉴를 해당 핸들러로 위임한다" {
                val f = Fixture()
                f.app.handleMenu(Menu.ACCOUNT)
                f.app.handleMenu(Menu.DEPOSIT)
                f.app.handleMenu(Menu.PURCHASE)
                f.app.handleMenu(Menu.DRAW)
                f.app.handleMenu(Menu.HISTORY)

                verify { f.accountHandler.handle() }
                verify { f.depositHandler.handle() }
                verify { f.purchaseHandler.handle() }
                verify { f.drawHandler.handle() }
                verify { f.historyHandler.handle() }
            }
            "EXIT는 도달 불가 상태이므로 예외를 던진다" {
                val f = Fixture()
                shouldThrow<IllegalStateException> { f.app.handleMenu(Menu.EXIT) }
            }
        }

        "App.run" should {
            "EXIT가 입력될 때까지 메뉴를 처리한다" {
                val f = Fixture()
                every { f.inputView.inputMenu() } returnsMany listOf(Menu.ACCOUNT, Menu.EXIT)

                f.app.run()

                verify { f.accountHandler.handle() }
            }
            "IllegalArgumentException이 발생하면 에러를 출력하고 계속한다" {
                every { Fixture().inputView.inputMenu() } throws IllegalArgumentException("잘못된 입력") andThen Menu.EXIT
            }
        }
    })
