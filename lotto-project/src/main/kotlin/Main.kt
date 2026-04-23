import lotto.App
import lotto.model.Account
import lotto.view.ConsoleInput
import lotto.view.ConsoleOutput
import lotto.view.InputView
import lotto.view.OutputView

fun Int.toFormattedString(): String = String.format("%,d", this)

fun main() {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))

    val app =
        App(
            Account(0),
            InputView(ConsoleInput()),
            OutputView(ConsoleOutput()),
        )

    app.run()
}
