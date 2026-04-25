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

    fun inputManualTicketNumbers(): LottoNumbers.Full {
        val line = readlnOrNull() ?: throw IllegalArgumentException("올바른 문장을 입력해주세요")
        return LottoNumbers.Full(parseNumbers(line))
    }

    fun inputSemiAutoTicketNumbers(): LottoNumbers.Half {
        val line = readlnOrNull() ?: throw IllegalArgumentException("올바른 문장을 입력해주세요")
        return LottoNumbers.Half(parseNumbers(line))
    }

    private fun parseNumbers(line: String): List<LottoNumber> {
        val list =
            line
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }
                .map { it.toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.") }
                .map { LottoNumber(it) }
        require(list.size == list.toSet().size) { "중복된 번호가 있습니다." }
        return list
    }
}
