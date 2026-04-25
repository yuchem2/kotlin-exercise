package lotto.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class LottoTickets(
    private val tickets: List<LottoNumbers>,
) : Iterable<LottoNumbers> {
    constructor() : this(emptyList())

    val size: Int get() = tickets.size

    override fun iterator(): Iterator<LottoNumbers> = tickets.iterator()

    operator fun plus(other: LottoTickets) = LottoTickets(tickets + other.tickets)
}
