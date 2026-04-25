package lotto.view

import lotto.model.LottoNumber
import lotto.model.LottoNumbers
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

    fun inputManualTicketNumbers(): LottoNumbers.Full {
        val line = input.readLine() ?: throw IllegalArgumentException("번호를 입력해주세요")
        return LottoNumbers.Full(parseNumbers(line))
    }

    fun inputSemiAutoTicketNumbers(): LottoNumbers.Half {
        val line = input.readLine() ?: throw IllegalArgumentException("번호를 입력해주세요")
        return LottoNumbers.Half(parseNumbers(line))
    }

    private fun parseNumbers(line: String): List<LottoNumber> {
        val list = line
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .map { it.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.") }
            .map { LottoNumber(it) }
        require(list.size == list.toSet().size) { "중복된 번호가 있습니다." }
        return list
    }
}
