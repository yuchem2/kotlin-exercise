package lotto.handler

import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.Account
import lotto.service.LottoService
import lotto.view.InputView
import lotto.view.OutputView

class PurchaseHandler(
    private val account: Account,
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            outputView.printGuidance("구매할 티켓의 양을 입력하세요: ")
            val count = inputView.inputTicketCount()
            outputView.printGuidance("수동으로 입력할 티켓의 양을 입력하세요: ")
            val manualCount = inputView.inputTicketCount()
            outputView.printGuidance("반자동으로 입력할 티켓의 양을 입력하세요: ")
            val semiAutoCount = inputView.inputTicketCount()

            if (manualCount + semiAutoCount > count) throw IllegalArgumentException("처음 입력한 티켓의 양보다 수동 + 반자동 티켓의 양이 많습니다.")

            val manualTicketNumbers =
                List(manualCount) {
                    outputView.printGuidance("수동으로 입력할 번호(${MIN_NUMBER}-${MAX_NUMBER})를 공백으로 구분해서 ${TICKET_SIZE}개 입력하세요: ")
                    inputView.inputManualTicketNumbers()
                }
            val semiAutoCountNumbers =
                List(semiAutoCount) {
                    outputView.printGuidance("반자동으로 입력할 번호(${MIN_NUMBER}-${MAX_NUMBER})를 공백으로 구분해서 ${TICKET_SIZE}개 미만으로 입력하세요: ")
                    inputView.inputManualTicketNumbers()
                }
            val tickets = LottoService.purchaseAndSave(account, count, manualTicketNumbers, semiAutoCountNumbers)
            // outputView.printMessage(tickets.mapIndexed { index, ticket -> "#${index + 1}: $ticket" }.joinToString("\n"))
            outputView.printMessage("구매가 완료되었습니다.")
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }
}
