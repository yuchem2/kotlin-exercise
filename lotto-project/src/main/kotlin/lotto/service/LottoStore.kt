package lotto.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lotto.constant.LOTTO_STORE_PATH
import lotto.model.LottoDraw
import java.io.File

object LottoStore {
    private var draws: MutableList<LottoDraw>

    init {
        val data = File(LOTTO_STORE_PATH).takeIf { it.exists() } ?.readText()

        draws =
            if (data == null) {
                mutableListOf()
            } else {
                Json.decodeFromString<MutableList<LottoDraw>>(data)
            }
    }

    fun loadAll(): List<LottoDraw> = draws

    fun save(draw: LottoDraw) {
        draws.add(draw)

        File(LOTTO_STORE_PATH).writeText(Json.encodeToString(draws))
    }

    fun getLastRound(): Int = draws.maxOfOrNull { it.round } ?: 0

    fun getByRound(round: Int): LottoDraw? = draws.find { it.round == round }
}
