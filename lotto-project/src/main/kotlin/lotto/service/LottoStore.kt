package lotto.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lotto.constant.LOTTO_STORE_PATH
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import java.io.File

object LottoStore {
    private var draws: LottoDraws

    init {
        val data =
            File(LOTTO_STORE_PATH)
                .takeIf { it.exists() }
                ?.readText()

        draws =
            if (data == null) {
                LottoDraws(emptyList())
            } else {
                try {
                    Json.decodeFromString<LottoDraws>(data)
                } catch (e: Exception) {
                    val file = File(LOTTO_STORE_PATH)
                    val backup = File("$LOTTO_STORE_PATH.bak")
                    file.renameTo(backup)

                    println("파일이 손상되어 백업 후 초기화됨")
                    LottoDraws(emptyList())
                }
            }
    }

    private fun persist() {
        val file = File(LOTTO_STORE_PATH)
        file.parentFile?.mkdirs()
        file.writeText(Json.encodeToString(draws))
    }

    fun save(draw: LottoDraw) {
        draws = draws.append(draw)
        persist()
    }

    fun updateLast(draw: LottoDraw) {
        if (draws.isEmpty()) return
        draws = draws.updateLast(draw)
        persist()
    }

    fun getLastRound(): Int = draws.lastRound()

    fun getAll(): LottoDraws = draws

    fun getByRound(round: Int): LottoDraw? = draws.findByRound(round)

    fun getLast(): LottoDraw? = draws.last()
}
