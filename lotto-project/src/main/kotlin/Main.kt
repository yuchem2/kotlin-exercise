import lotto.App
import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.service.AccountStore
import lotto.view.InputView
import lotto.view.OutputView
import java.io.PrintStream

private fun setupEncoding() {
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setErr(PrintStream(System.err, true, "UTF-8"))
}

fun main() {
    setupEncoding()

    val accountStore = AccountStore()
    val inputView = InputView()
    val outputView = OutputView()

    val app =
        App(
            inputView,
            outputView,
            accountHandler = AccountHandler(accountStore, outputView),
            depositHandler = DepositHandler(accountStore, inputView, outputView),
            purchaseHandler = PurchaseHandler(accountStore, inputView, outputView),
            drawHandler = DrawHandler(accountStore, outputView),
            historyHandler = HistoryHandler(inputView, outputView),
        )

    app.run()
}
