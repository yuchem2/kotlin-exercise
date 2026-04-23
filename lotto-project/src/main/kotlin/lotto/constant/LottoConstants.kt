package lotto.constant

import io.github.cdimascio.dotenv.dotenv

val env =
    dotenv {
        directory = "./"
        ignoreIfMissing = true
    }

const val TICKET_PRICE = 1000
const val MIN_NUMBER = 1
const val MAX_NUMBER = 45
const val TICKET_SIZE = 6
const val BONUS_COUNT = 1
val LOTTO_STORE_PATH = env["LOTTO_STORE_PATH"] ?: "lotto.json"
