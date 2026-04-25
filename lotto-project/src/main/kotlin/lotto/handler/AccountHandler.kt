package lotto.handler

import lotto.service.AccountService
import lotto.view.OutputView

class AccountHandler(
    private val accountService: AccountService,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        outputView.printBalance(accountService.getAmount())
    }
}
