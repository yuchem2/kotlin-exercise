package lotto.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

private fun numbers(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

private val winning = WinningNumbers(numbers(1, 2, 3, 4, 5, 6), LottoNumber(7))

private fun drawWith(ticket: LottoNumbers) = LottoDraw(round = 1, tickets = LottoTickets(listOf(ticket)))

class LottoDrawTest :
    WordSpec({
        "진행 중인 LottoDraw" should {
            "isEnded는 false다" {
                drawWith(numbers(1, 2, 3, 4, 5, 6)).isEnded() shouldBe false
            }
            "toString은 진행 중으로 표시한다" {
                drawWith(numbers(1, 2, 3, 4, 5, 6)).toString() shouldContain "진행 중"
            }
            "getSummary는 진행 중 안내를 포함한다" {
                drawWith(numbers(1, 2, 3, 4, 5, 6)).getSummary() shouldContain "아직 진행 중인 회차입니다."
            }
        }

        "LottoDraw.setResult" should {
            "당첨번호가 없으면 종료되지 않는다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.setResult()
                draw.isEnded() shouldBe false
            }
        }

        "LottoDraw.addTicket" should {
            "종료 전에는 티켓을 추가한다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.addTicket(LottoTickets(listOf(numbers(10, 11, 12, 13, 14, 15))))
                draw.getSummary() shouldContain "총 티켓: 2"
            }
            "종료 후에는 티켓을 추가하면 예외를 던진다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.endDraw(winning)
                shouldThrow<IllegalStateException> {
                    draw.addTicket(LottoTickets(listOf(numbers(10, 11, 12, 13, 14, 15))))
                }
                draw.getSummary() shouldContain "총 티켓: 1"
            }
        }

        "LottoDraw.endDraw" should {
            "당첨 결과와 수익을 계산하고 종료한다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.endDraw(winning)

                draw.isEnded() shouldBe true
                draw.getResult().result[LottoRank.FIRST] shouldBe 1
                draw.getResult().totalIncome shouldBe LottoRank.FIRST.prize
            }
            "종료된 회차의 toString과 getSummary는 결과를 보여준다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.endDraw(winning)

                draw.toString() shouldContain "#1"
                draw.getSummary() shouldContain "1등: 1개"
            }
            "이미 종료된 회차는 다시 종료해도 결과가 유지된다" {
                val draw = drawWith(numbers(1, 2, 3, 4, 5, 6))
                draw.endDraw(winning)
                draw.endDraw(WinningNumbers(numbers(10, 11, 12, 13, 14, 15), LottoNumber(20)))

                draw.getResult().result[LottoRank.FIRST] shouldBe 1
            }
        }

        "LottoResult.toString" should {
            "등수별 개수와 총수익을 이어붙인다" {
                val result = LottoResult(mapOf(LottoRank.FIFTH to 2), 10_000L)
                result.toString() shouldContain "2"
                result.toString() shouldContain "1만"
            }
        }
    })
