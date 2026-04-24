package lotto.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lotto.constant.ACCOUNT_STORE_PATH
import lotto.model.Account
import java.io.File

class AccountStore {
    private var account: Account

    init {
        val data =
            File(ACCOUNT_STORE_PATH)
                .takeIf { it.exists() }
                ?.readText()

        val amount =
            if (data == null) {
                0
            } else {
                try {
                    Json.decodeFromString<Int>(data)
                } catch (e: Exception) {
                    val file = File(ACCOUNT_STORE_PATH)
                    val backup = File("$ACCOUNT_STORE_PATH.bak")
                    file.renameTo(backup)

                    println("파일이 손상되어 백업 후 초기화됨")
                    0
                }
            }
        account = Account(amount)
    }

    fun persist() {
        val file = File(ACCOUNT_STORE_PATH)
        file.parentFile?.mkdirs()
        file.writeText(Json.encodeToString(account.getAmount()))
    }

    fun getAmount() = account.getAmount()

    fun deposit(amount: Int) {
        account.deposit(amount)
        persist()
    }

    fun withdraw(amount: Int) {
        account.withdraw(amount)
        persist()
    }
}
