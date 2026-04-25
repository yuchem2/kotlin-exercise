package lotto.model

import kotlinx.serialization.Serializable

@Serializable
class WinningNumbers(
    val mainNumbers: LottoNumbers,
    val bonus: LottoNumber,
) {
    init {
        require(bonus !in mainNumbers) { "보너스 숫자는 당첨번호에 없는 숫자여야 합니다." }
    }
}
