package lotto.model

import kotlinx.serialization.Serializable
import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE

@Serializable
data class LottoTicket(
    val numbers: List<Int>,
) {
    init {
        require(numbers.size == TICKET_SIZE) { "로또 번호는 ${TICKET_SIZE}개여야 합니다" }
        require(numbers.all { it in MIN_NUMBER..MAX_NUMBER }) { "로또 번호는 ${MIN_NUMBER}와 $MAX_NUMBER 사이여야 합니다" }
        require(numbers.toSet().size == TICKET_SIZE) { "로또 번호에 중복은 없어야 합니다" }
    }

    fun compare(target: LottoTicket?): Int = if (target != null) numbers.intersect(target.numbers.toSet()).size else 0

    fun hasBonusNumbers(target: List<Int>?): Int = if (target != null) numbers.intersect(target.toSet()).size else 0

    override fun toString(): String = numbers.joinToString(separator = ", ")

}
