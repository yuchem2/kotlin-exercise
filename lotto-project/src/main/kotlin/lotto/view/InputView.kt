package lotto.view

import lotto.model.Menu

interface InputPort {
    fun readLine(): String?
}

class ConsoleInput : InputPort {
    override fun readLine() = readlnOrNull()
}

class InputView(
    private val input: InputPort,
) {
    fun inputMenu(): Menu {
        val number =
            input.readLine()?.toIntOrNull()
                ?: throw IllegalArgumentException("숫자를 입력해주세요.")
        return Menu.entries.find { it.number == number }
            ?: throw IllegalArgumentException("없는 메뉴입니다.")
    }

    fun inputNumber(): Int {
        val value =
            input.readLine()?.toIntOrNull()
                ?: throw IllegalArgumentException("숫자를 입력해주세요.")
        return value
    }

    fun inputAmount() = inputNumber()

    fun inputTicketCount() = inputNumber()

    fun inputManualTicketNumbers(): List<Int> {
        val values = input.readLine() ?: throw IllegalArgumentException("번호를 입력해주세요")
        return values
            .split(" ")
            .map { it.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.") }
    }
}
