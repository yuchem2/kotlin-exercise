package lotto.handler

import lotto.service.AccountService
import lotto.view.InputView
import lotto.view.OutputView

class DepositHandler(
    private val accountService: AccountService,
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            outputView.printGuidance("입금할 금액을 입력하세요: ")
            val amount = inputView.inputPositive().toLong()
            accountService.deposit(amount)
            outputView.printBalance(accountService.getAmount())
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }
}
