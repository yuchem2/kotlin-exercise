package lotto.handler

import lotto.service.AccountStore
import lotto.service.LottoService
import lotto.view.OutputView

class DrawHandler(
    private val account: AccountStore,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        val lastRound = LottoService.endAndSave(account)
        if (lastRound == null) {
            outputView.printMessage("진행되고 있는 회차가 없거나, 이미 종료되었습니다.")
            return
        }
        outputView.printMessage(lastRound.getSummary())
    }
}
