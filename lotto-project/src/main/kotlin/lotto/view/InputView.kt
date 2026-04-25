package lotto.view

import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.model.Menu

class InputView {
    fun inputMenu(): Menu = Menu.from(inputNumber())

    fun inputNumber(): Int = readlnOrNull()?.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.")

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

    fun inputTicketNumbers(): LottoNumbers {
        val line = readlnOrNull() ?: throw IllegalArgumentException("올바른 문장을 입력해주세요")
        return LottoNumbers(parseNumbers(line))
    }

    private fun parseNumbers(line: String): List<LottoNumber> =
        line
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .map { it.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.") }
            .map { LottoNumber(it) }
}
