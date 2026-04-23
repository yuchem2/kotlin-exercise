package lotto.handler

import lotto.model.Account
import lotto.service.LottoService
import lotto.service.LottoStore
import lotto.view.OutputView

class DrawHandler(
    private val account: Account,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) {
            outputView.printMessage("진행되고 있는 회차가 없거나, 진행중인 회차가 이미 종료되었습니다.")
            return
        }
        LottoService.endDraw(lastRound)
        LottoStore.updateLast(lastRound)
        account.deposit(lastRound.getResult().totalIncome)
        outputView.printHistory(lastRound.toString())
    }
}
