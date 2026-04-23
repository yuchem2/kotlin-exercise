package lotto.view

import lotto.model.Menu

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
    private val output: OutputPort
) {
    fun printWelcome() {
        output.println("======================")
        output.println("    로또 시스템 🎰    ")
        output.println("======================")
    }

    fun printMenu() {
        val items = Menu.entries.map { "${it.number}. ${it.description}" }
        output.printMenu(items)
    }

    fun printGuidance(message: String) = output.print(message)

    fun printMessage(message: String) = output.println(message)

    fun printError(message: String?) = output.println("[ERROR] ${message ?: "예기치 못한 오류입니다."}")
}
