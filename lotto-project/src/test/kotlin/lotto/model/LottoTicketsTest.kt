package lotto.model

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

private fun oneTicket(start: Int) = LottoNumbers((start until start + 6).map { LottoNumber(it) })

class LottoTicketsTest :
    WordSpec({
        "LottoTickets 기본 생성자" should {
            "빈 티켓 묶음을 만든다" {
                LottoTickets().size shouldBe 0
            }
        }

        "LottoTickets.size" should {
            "보유한 티켓 수를 반환한다" {
                LottoTickets(listOf(oneTicket(1), oneTicket(10))).size shouldBe 2
            }
        }

        "LottoTickets 순회" should {
            "보유한 티켓을 순회할 수 있다" {
                val tickets = LottoTickets(listOf(oneTicket(1), oneTicket(10)))
                tickets.toList().size shouldBe 2
            }
        }

        "LottoTickets.plus" should {
            "두 묶음을 합친다" {
                val combined = LottoTickets(listOf(oneTicket(1))) + LottoTickets(listOf(oneTicket(10)))
                combined.size shouldBe 2
            }
        }
    })
