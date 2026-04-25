package lotto.strategy

import lotto.generator.RandomNumberGenerator

internal class Auto : NumberStrategy {
    override fun pick() = RandomNumberGenerator.pick()
}
