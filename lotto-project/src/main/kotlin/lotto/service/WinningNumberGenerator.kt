package lotto.service

import lotto.model.LottoNumbers
import lotto.model.WinningNumbers

object WinningNumberGenerator {
    fun generate(): WinningNumbers {
        val numbers = RandomNumberGenerator.pick(LottoNumbers.Full.SIZE + 1)
        val bonus = numbers.last()
        val winnings = numbers.dropLast(1)
        return WinningNumbers(LottoNumbers.Full(winnings), bonus)
    }
}
