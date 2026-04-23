package lotto.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lotto.constant.LOTTO_STORE_PATH
import lotto.model.LottoDraw
import java.io.File

object LottoStore {
    private var draws: MutableList<LottoDraw>

    init {
        val data =
            File(LOTTO_STORE_PATH)
                .takeIf { it.exists() }
                ?.readText()

        draws =
            if (data == null) {
                mutableListOf()
            } else {
                try {
                    Json.decodeFromString<MutableList<LottoDraw>>(data)
                } catch (e: Exception) {
                    File(LOTTO_STORE_PATH).renameTo(File("$LOTTO_STORE_PATH.bak"))
                    mutableListOf()
                }
            }
    }

    private fun persist() {
        val file = File(LOTTO_STORE_PATH)
        file.parentFile?.mkdirs()
        file.writeText(Json.encodeToString(draws))
    }

    fun save(draw: LottoDraw) {
        draws.add(draw)
        persist()
    }

    fun updateLast(draw: LottoDraw) {
        if (draws.isEmpty()) return
        draws[draws.lastIndex] = draw
        persist()
    }

    fun getLastRound(): Int = draws.maxOfOrNull { it.round } ?: 0

    fun getAll(): List<LottoDraw> = draws.toList()

    fun getByRound(round: Int): LottoDraw? = draws.find { it.round == round }

    fun getLast(): LottoDraw? = draws.lastOrNull()
}
