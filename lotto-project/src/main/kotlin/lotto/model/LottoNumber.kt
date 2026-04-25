package lotto.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class LottoNumber(
    val number: Int,
) {
    init {
        require(number in MIN..MAX) { "로또 번호는 ${MIN}와 $MAX 사이여야 합니다" }
    }

    companion object {
        const val MIN = 1
        const val MAX = 45
    }
}

@Serializable
sealed class LottoNumbers {
    abstract val numbers: List<LottoNumber>
    val size: Int get() = numbers.size

    fun count(other: LottoNumbers): Int = numbers.count { it in other.numbers }

    override fun toString(): String = numbers.joinToString(separator = ", ")

    operator fun contains(number: LottoNumber) = numbers.contains(number)

    operator fun contains(number: Int) = contains(LottoNumber(number))

    @Serializable
    @SerialName("full")
    class Full(
        override val numbers: List<LottoNumber>,
    ) : LottoNumbers() {
        init {
            require(numbers.size == SIZE) { "로또 번호는 ${SIZE}개여야 합니다" }
        }

        companion object {
            const val SIZE = 6
        }
    }

    @Serializable
    @SerialName("half")
    class Half(
        override val numbers: List<LottoNumber>,
    ) : LottoNumbers() {
        init {
            require(numbers.isNotEmpty()) { "로또 번호는 비어있으면 안됩니다." }
            require(numbers.size < Full.SIZE) { "로또 번호는 ${Full.SIZE}개 미만이어야 합니다" }
        }

        operator fun plus(other: Half) = Full(numbers + other.numbers)
    }
}
