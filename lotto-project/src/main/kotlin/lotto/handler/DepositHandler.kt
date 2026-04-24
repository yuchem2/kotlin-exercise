package lotto.handler

import lotto.service.AccountStore
import lotto.view.InputView
import lotto.view.OutputView

class DepositHandler(
    private val account: AccountStore,
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            outputView.printGuidance("입급할 금액을 입력하세요: ")
            val amount = inputView.inputAmount()
            account.deposit(amount)
            outputView.printBalance(account.getAmount())
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }
}
