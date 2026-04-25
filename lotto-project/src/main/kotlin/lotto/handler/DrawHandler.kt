package lotto.handler

import lotto.service.LottoService
import lotto.view.OutputView

class DrawHandler(
    private val lottoService: LottoService,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        val lastRound = lottoService.endAndSave()
        if (lastRound == null) {
            outputView.printMessage("진행되고 있는 회차가 없거나, 이미 종료되었습니다.")
            return
        }
        outputView.printMessage(lastRound.getSummary())
    }
}
