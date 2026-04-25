package lotto.model

import kotlinx.serialization.Serializable
import lotto.strategy.NumberStrategy

@Serializable
@JvmInline
value class LottoNumber(
    val number: Int,
) {
    init {
        require(number in MIN..MAX) { "로또 번호는 ${MIN}과 $MAX 사이여야 합니다" }
    }

    companion object {
        const val MIN = 1
        const val MAX = 45
        val ALL_NUMBERS = (MIN..MAX).map { LottoNumber(it) }
    }
}

@Serializable
@JvmInline
value class LottoNumbers(
    val numbers: List<LottoNumber>,
) {
    val size: Int get() = numbers.size

    init {
        require(numbers.toSet().size == numbers.size) { "번호는 중복될 수 없습니다" }
        require(numbers.size == FULL_SIZE) { "로또 번호는 ${FULL_SIZE}개여야 합니다" }
    }

    fun count(other: LottoNumbers): Int = numbers.count { it in other.numbers }

    override fun toString(): String = numbers.joinToString(separator = ", ")

    operator fun contains(number: LottoNumber) = numbers.contains(number)

    operator fun plus(other: LottoNumbers): LottoNumbers = LottoNumbers(numbers + other.numbers)

    companion object {
        const val FULL_SIZE = 6
    }
}
