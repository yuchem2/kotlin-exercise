import lotto.App
import lotto.handler.AccountHandler
import lotto.handler.DepositHandler
import lotto.handler.DrawHandler
import lotto.handler.HistoryHandler
import lotto.handler.PurchaseHandler
import lotto.repository.AccountRepository
import lotto.repository.LottoDrawRepository
import lotto.service.AccountService
import lotto.service.LottoService
import lotto.view.InputView
import lotto.view.OutputView
import java.io.PrintStream

private fun setupEncoding() {
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setErr(PrintStream(System.err, true, "UTF-8"))
}

fun main() {
    setupEncoding()

    val accountRepository = AccountRepository()
    val drawRepository = LottoDrawRepository()
    val accountService = AccountService(accountRepository)
    val lottoService = LottoService(accountService, drawRepository)
    val inputView = InputView()
    val outputView = OutputView()

    val app =
        App(
            inputView,
            outputView,
            accountHandler = AccountHandler(accountService, outputView),
            depositHandler = DepositHandler(accountService, inputView, outputView),
            purchaseHandler = PurchaseHandler(lottoService, inputView, outputView),
            drawHandler = DrawHandler(lottoService, outputView),
            historyHandler = HistoryHandler(lottoService, inputView, outputView),
        )

    app.run()
}
