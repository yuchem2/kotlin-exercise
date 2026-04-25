package lotto.view

import lotto.model.Menu
import lotto.util.padEndKo
import lotto.util.toFormattedString

class OutputView {
    fun printWelcome() {
        println("==========================")
        println("      로또 시스템        ")
        println("==========================")
    }

    fun printMenu() = Menu.entries.map { "${it.number}. ${it.description}" }.forEach { println(it) }

    fun printGuidance(message: String) = print(message)

    fun printMessage(message: String) = println(message)

    fun printError(message: String?) = println("[ERROR] ${message ?: "예기치 못한 오류입니다."}")

    fun printDrawHeader() {
        val header =
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
        println(header)
    }

    fun printHistory(message: String) {
        println("=".repeat(120))
        printDrawHeader()
        println(message)
        println("=".repeat(120))
    }

    fun printBalance(amount: Long) {
        println("현재 잔액: ${amount.toFormattedString()}원")
    }
}
