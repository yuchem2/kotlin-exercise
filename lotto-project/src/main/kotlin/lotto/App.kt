package lotto

import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.model.Menu
import lotto.view.InputView
import lotto.view.OutputView

class App(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val accountHandler: AccountHandler,
    private val depositHandler: DepositHandler,
    private val purchaseHandler: PurchaseHandler,
    private val drawHandler: DrawHandler,
    private val historyHandler: HistoryHandler,
) {
    fun run() {
        while (true) {
            try {
                outputView.printWelcome()
                outputView.printMenu()
                when (val menu = inputView.inputMenu()) {
                    Menu.EXIT -> break
                    else -> handleMenu(menu)
                }
            } catch (e: IllegalArgumentException) {
                outputView.printError(e.message ?: "잘못된 입력입니다.")
            }
        }
    }

    fun handleMenu(menu: Menu) {
        when (menu) {
            Menu.ACCOUNT -> {
                accountHandler.handle()
            }

            Menu.DEPOSIT -> {
                depositHandler.handle()
            }

            Menu.PURCHASE -> {
                purchaseHandler.handle()
            }

            Menu.DRAW -> {
                drawHandler.handle()
            }

            Menu.HISTORY -> {
                historyHandler.handle()
            }

            Menu.EXIT -> {
                throw IllegalStateException("도달 불가")
            }
        }
    }
}
