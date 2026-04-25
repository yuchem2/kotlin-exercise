package lotto.handler

import lotto.service.LottoService
import lotto.view.InputView
import lotto.view.OutputView

class HistoryHandler(
    private val lottoService: LottoService,
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            outputView.printGuidance("조회할 회차를 입력하세요(0: 전체): ")
            val round = inputView.inputNonNegative()
            if (round == 0) printHistory() else printHistoryByRound(round)
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    private fun printHistory() {
        val history = lottoService.getAllHistory()
        if (history.isNotEmpty()) {
            outputView.printHistory(history.toDisplayString())
        } else {
            outputView.printMessage("진행된 회차가 없습니다.")
        }
    }

    private fun printHistoryByRound(round: Int) {
        require(round > 0) { "회차 번호는 양수여야 합니다." }

        val history = lottoService.getHistoryByRound(round)
        if (history == null) {
            outputView.printError("잘못된 회차 번호입니다.")
        } else {
            outputView.printMessage(history.getSummary())
        }
    }
}
