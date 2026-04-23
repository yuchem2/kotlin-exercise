import lotto.App
import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.model.Account
import lotto.view.ConsoleInput
import lotto.view.ConsoleOutput
import lotto.view.InputView
import lotto.view.OutputView

private fun setupEncoding() {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))
    System.setErr(java.io.PrintStream(System.err, true, "UTF-8"))
}

fun main() {
    setupEncoding()

    val account = Account(0)
    val inputView = InputView(ConsoleInput())
    val outputView = OutputView(ConsoleOutput())

    val app =
        App(
            inputView,
            outputView,
            accountHandler = AccountHandler(account, outputView),
            depositHandler = DepositHandler(account, inputView, outputView),
            purchaseHandler = PurchaseHandler(account, inputView, outputView),
            drawHandler = DrawHandler(account, outputView),
            historyHandler = HistoryHandler(inputView, outputView),
        )

    app.run()
}
