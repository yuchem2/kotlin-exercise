package lotto.view

import lotto.model.Menu
import lotto.util.padEndKo
import lotto.util.toFormattedString

interface OutputPort {
    fun print(message: String)

    fun println(message: String)

    fun printMenu(items: List<String>)
}

class ConsoleOutput : OutputPort {
    override fun print(message: String) = kotlin.io.print(message)

    override fun println(message: String) = kotlin.io.println(message)

    override fun printMenu(items: List<String>) = items.forEach { println(it) }
}

class OutputView(
    private val output: OutputPort,
) {
    fun printWelcome() {
        output.println("==========================")
        output.println("      로또 시스템        ")
        output.println("==========================")
    }

    fun printMenu() {
        val items = Menu.entries.map { "${it.number}. ${it.description}" }
        output.printMenu(items)
    }

    fun printGuidance(message: String) = output.print(message)

    fun printMessage(message: String) = output.println(message)

    fun printError(message: String?) = output.println("[ERROR] ${message ?: "예기치 못한 오류입니다."}")

    fun printDrawHeader() {
        val output =
            buildString {
                append("회차".padEndKo(6))
                append("총티켓".padEndKo(14))
                append("1등".padEndKo(14))
                append("2등".padEndKo(14))
                append("3등".padEndKo(14))
                append("4등".padEndKo(14))
                append("5등".padEndKo(14))
                append("낙첨".padEndKo(16))
                append("총수익".padEndKo(20))
            }
        println(output)
    }

    fun printHistory(message: String) {
        output.println("=".repeat(120))
        printDrawHeader()
        output.println(message)
        output.println("=".repeat(120))
    }

    fun printBalance(amount: Int) {
        output.println("현재 잔액: ${amount.toFormattedString()}원")
    }
}
