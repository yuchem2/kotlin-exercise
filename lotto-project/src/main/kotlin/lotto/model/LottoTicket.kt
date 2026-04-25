package lotto.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class LottoTicket(
    val numbers: LottoNumbers,
) {
    init {
        numbers.requireFull()
    }

    operator fun contains(number: LottoNumber) = number in numbers

    override fun toString(): String = numbers.toString()
}
