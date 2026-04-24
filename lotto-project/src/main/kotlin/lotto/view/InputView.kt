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

    fun inputNonNegative(): Int {
        val value = inputNumber()
        require(value >= 0) { "0 이상의 수를 입력해주세요." }
        return value
    }

    fun inputPositive(): Int {
        val value = inputNumber()
        require(value > 0) { "1 이상의 수를 입력해주세요." }
        return value
    }

    fun inputManualTicketNumbers(): List<Int> {
        val values = input.readLine() ?: throw IllegalArgumentException("번호를 입력해주세요")
        return values
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .map { it.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.") }
    }
}
