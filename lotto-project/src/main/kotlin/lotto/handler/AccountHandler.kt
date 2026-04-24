package lotto.handler

import lotto.service.AccountStore
import lotto.view.OutputView

class AccountHandler(
    private val account: AccountStore,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        outputView.printBalance(account.getAmount())
    }
}
