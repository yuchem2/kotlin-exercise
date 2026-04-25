package lotto.repository

import kotlinx.serialization.builtins.serializer
import lotto.constant.ACCOUNT_STORE_PATH
import lotto.model.Account

class AccountRepository(
    private val storage: JsonFileStorage<Long> =
        JsonFileStorage(
            path = ACCOUNT_STORE_PATH,
            serializer = Long.serializer(),
            default = { 0L },
        ),
) {
    fun load(): Account = Account(storage.load())

    fun save(account: Account) = storage.save(account.amount)
}
