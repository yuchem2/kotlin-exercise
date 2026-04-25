package lotto.repository

import lotto.constant.LOTTO_STORE_PATH
import lotto.model.LottoDraws

class LottoDrawRepository(
    private val storage: JsonFileStorage<LottoDraws> =
        JsonFileStorage(
            path = LOTTO_STORE_PATH,
            serializer = LottoDraws.serializer(),
            default = { LottoDraws() },
        ),
) {
    private var draws: LottoDraws = storage.load()

    fun load(): LottoDraws = draws

    fun save(draws: LottoDraws) {
        this.draws = draws
        storage.save(draws)
    }
}
