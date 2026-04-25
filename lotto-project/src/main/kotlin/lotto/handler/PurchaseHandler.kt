package lotto.handler

import lotto.constant.TICKET_PRICE
import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.service.AccountStore
import lotto.service.LottoService
import lotto.strategy.AutoStrategy
import lotto.strategy.ManualStrategy
import lotto.strategy.SemiAutoStrategy
import lotto.strategy.TicketStrategy
import lotto.view.InputView
import lotto.view.OutputView

class PurchaseHandler(
    private val account: AccountStore,
    private val inputView: InputView,
    private val outputView: OutputView,
) : Handler {
    override fun handle() {
        try {
            val count = inputCount()
            val manualCount = inputManualCount(count)
            val semiAutoCount = inputSemiAutoCount(count, manualCount)
            val manualTicketNumbers = inputManualTicketNumbers(manualCount)
            val semiAutoTicketNumbers = inputSemiAutoTicketNumbers(semiAutoCount)
            val strategies = buildStrategies(count, manualTicketNumbers, semiAutoTicketNumbers)

            LottoService.purchaseAndSave(account, strategies)
            outputView.printMessage("구매가 완료되었습니다.")
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    private fun inputCount(): Int {
        outputView.printGuidance("구매할 티켓의 양을 입력하세요: ")
        val count = inputView.inputPositive()
        require(account.getAmount() >= count * TICKET_PRICE) { "소지한 금액보다 티켓 구매 비용이 많습니다." }
        return count
    }

    private fun inputManualCount(count: Int): Int {
        outputView.printGuidance("수동으로 입력할 티켓의 양을 입력하세요: ")
        val manualCount = inputView.inputNonNegative()
        require(manualCount <= count) { "입력한 총 티켓의 양보다 수동 입력 티켓의 양이 많습니다." }
        return manualCount
    }

    private fun inputSemiAutoCount(
        count: Int,
        manualCount: Int,
    ): Int {
        outputView.printGuidance("반자동으로 입력할 티켓의 양을 입력하세요: ")
        val semiAutoCount = inputView.inputNonNegative()
        require(manualCount + semiAutoCount <= count) { "입력한 총 티켓의 양보다 수동 + 반자동 티켓의 양이 많습니다." }
        return semiAutoCount
    }

    private fun inputManualTicketNumbers(count: Int): List<LottoNumbers.Full> =
        List(count) {
            outputView.printGuidance("수동 번호(${LottoNumber.MIN}-${LottoNumber.MAX}) ${LottoNumbers.Full}개 입력: ")
            inputView.inputManualTicketNumbers()
        }

    private fun inputSemiAutoTicketNumbers(count: Int): List<LottoNumbers.Half> =
        List(count) {
            outputView.printGuidance("반자동 번호(${LottoNumber.MIN}-${LottoNumber.MAX}) ${LottoNumbers.Full}개 미만 입력: ")
            inputView.inputSemiAutoTicketNumbers()
        }

    private fun buildStrategies(
        count: Int,
        manualInputs: List<LottoNumbers.Full>,
        semiAutoInputs: List<LottoNumbers.Half>,
    ): List<TicketStrategy> =
        manualInputs.map { ManualStrategy(it) } + semiAutoInputs.map { SemiAutoStrategy(it) } +
            List(count - manualInputs.size - semiAutoInputs.size) { AutoStrategy() }
}
