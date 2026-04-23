package lotto.handler

import lotto.model.Account
import lotto.view.OutputView

class AccountHandler(
    private val account: Account,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        outputView.printBalance(account.getAmount())
    }
}
