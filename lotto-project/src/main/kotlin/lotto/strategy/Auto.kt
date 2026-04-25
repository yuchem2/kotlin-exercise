package lotto.strategy

import lotto.service.RandomNumberGenerator

internal class Auto : NumberStrategy {
    override fun pick() = RandomNumberGenerator.pick()
}
