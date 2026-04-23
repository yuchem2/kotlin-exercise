package lotto.handler

import lotto.service.LottoStore
import lotto.view.InputView
import lotto.view.OutputView

class HistoryHandler(
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            outputView.printGuidance("조회할 회차를 입력하세요(0: 전체): ")
            val round = inputView.inputNumber()
            if (round == 0) printHistory() else printHistoryByRound(round)
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    private fun printHistory() {
        val history = LottoStore.getAll()
        if (history.isNotEmpty()) {
            outputView.printHistory(history.joinToString("\n"))
        } else {
            outputView.printMessage("진행된 회차가 없습니다.")
        }
    }

    private fun printHistoryByRound(round: Int) {
        val history = LottoStore.getByRound(round)
        if (history == null) {
            outputView.printError("잘못된 회차 번호입니다.")
        } else {
            outputView.printDraw(history)
        }
    }
}
