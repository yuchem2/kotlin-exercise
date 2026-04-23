package lotto

import lotto.constant.MAX_NUMBER
import lotto.constant.MIN_NUMBER
import lotto.constant.TICKET_SIZE
import lotto.model.Account
import lotto.model.Menu
import lotto.service.LottoService
import lotto.service.LottoStore
import lotto.view.InputView
import lotto.view.OutputView
import toFormattedString
import kotlin.system.exitProcess

class App(
    private val account: Account,
    private val inputView: InputView,
    private val outputView: OutputView
) {
    fun run() {
        while (true) {
            try {
                outputView.printWelcome()
                outputView.printMenu()
                val menu = inputView.inputMenu()
                if (menu == Menu.EXIT) break
                handleMenu(menu, account)
            } catch (e: IllegalArgumentException) {
                outputView.printError(e.message ?: "잘못된 입력입니다.")
            }
        }
    }

    fun handleMenu(
        menu: Menu,
        account: Account,
    ) {
        when (menu) {
            Menu.DEPOSIT -> handleDeposit(account)
            Menu.PURCHASE -> handlePurchase(account)
            Menu.DRAW -> handleDraw(account)
            Menu.HISTORY -> handleHistory()
            Menu.ACCOUNT -> handleAccount(account)
            Menu.EXIT -> exitProcess(0)
        }
    }

    fun handleDeposit(account: Account) {
        try {
            outputView.printGuidance("입급할 금액을 입력하세요: ")
            val amount = inputView.inputAmount()
            account.deposit(amount)
            outputView.printMessage("남은 잔액: ${account.getAmount().toFormattedString()}")
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    fun handlePurchase(account: Account) {
        try {
            outputView.printGuidance("구매할 티켓의 양을 입력하세요: ")
            val count = inputView.inputTicketCount()
            outputView.printGuidance("수동으로 입력할 티켓의 양을 입력하세요(0: 전부자동): ")
            val manualCount = inputView.inputTicketCount()

            val manualTicketNumbers =
                List(manualCount) {
                    outputView.printMessage("수동으로 입력할 번호(${MIN_NUMBER}-${MAX_NUMBER})를 공백으로 구분해서 ${TICKET_SIZE}개 입력하세요: ")
                    inputView.inputManualTicketNumbers()
                }
            val tickets = LottoService.purchase(account, count, manualTicketNumbers)
            val lastRound = LottoStore.getLast()
            if (lastRound == null || lastRound.isEnded()) {
                val draw = LottoService.createDraw(LottoStore.getLastRound() + 1, tickets)
                LottoStore.save(draw)
            } else {
                lastRound.addTicket(tickets)
                LottoStore.updateLast(lastRound)
            }

            outputView.printMessage(tickets.mapIndexed { index, ticket -> "#${index + 1}: $ticket" }.joinToString("\n"))
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    fun handleDraw(account: Account) {
        val lastRound = LottoStore.getLast()
        if (lastRound == null || lastRound.isEnded()) {
            outputView.printMessage("진행되고 있는 회차가 없거나, 진행중인 회차가 이미 종료되었습니다.")
            return
        }
        LottoService.endDraw(lastRound)
        LottoStore.updateLast(lastRound)
        account.deposit(lastRound.getResult().totalIncome)
        printHistoryByRound()
    }

    fun handleHistory() {
        try {
            outputView.printGuidance("조회할 회차를 입력하세요(0: 전체): ")
            val round = inputView.inputNumber()

            if (round == 0) {
                val history = LottoStore.getAll()
                if (history.isNotEmpty()) {
                    printHistory(history.joinToString("\n"))
                } else {
                    outputView.printMessage("진행된 회차가 없습니다.")
                }
            } else {
                printHistoryByRound(round)
            }
        } catch (e: IllegalArgumentException) {
            outputView.printError(e.message)
        }
    }

    fun printHistoryByRound(round: Int = LottoStore.getLastRound()) {
        val history = LottoStore.getByRound(round)
        if (history == null) {
            outputView.printError("잘못된 회차 번호입니다.")
        } else {
            printHistory(history.toString())
        }
    }

    fun printHistory(message: String) {
        outputView.printMessage("=============================================")
        outputView.printMessage("회차\t총 티켓\t1등\t2등\t3등\t4등\t5등\t그외\t총수익")
        outputView.printMessage(message)
        outputView.printMessage("=============================================")
    }

    fun handleAccount(account: Account) {
        outputView.printMessage("현재 잔액: ${account.getAmount().toFormattedString()}")
    }
}
