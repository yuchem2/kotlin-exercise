package lotto.view

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.string.shouldContain
import lotto.support.captureStdout

class OutputViewTest :
    WordSpec({
        val view = OutputView()

        "OutputView" should {
            "printWelcome는 시스템 안내를 출력한다" {
                captureStdout { view.printWelcome() } shouldContain "로또 시스템"
            }
            "printMenu는 모든 메뉴 항목을 출력한다" {
                val output = captureStdout { view.printMenu() }
                output shouldContain "입금"
                output shouldContain "종료"
            }
            "printGuidance는 줄바꿈 없이 출력한다" {
                captureStdout { view.printGuidance("입력: ") } shouldContain "입력: "
            }
            "printMessage는 메시지를 출력한다" {
                captureStdout { view.printMessage("안녕") } shouldContain "안녕"
            }
            "printError는 ERROR 접두어를 붙인다" {
                captureStdout { view.printError("문제 발생") } shouldContain "[ERROR] 문제 발생"
            }
            "printError는 null이면 기본 메시지를 출력한다" {
                captureStdout { view.printError(null) } shouldContain "예기치 못한 오류입니다."
            }
            "printDrawHeader는 헤더 컬럼을 출력한다" {
                captureStdout { view.printDrawHeader() } shouldContain "총수익"
            }
            "printHistory는 헤더와 본문을 함께 출력한다" {
                captureStdout { view.printHistory("본문내용") } shouldContain "본문내용"
            }
            "printBalance는 잔액을 포맷해 출력한다" {
                captureStdout { view.printBalance(50_000) } shouldContain "현재 잔액: 5만원"
            }
        }
    })
