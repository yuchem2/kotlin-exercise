package lotto.model

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

private fun draw(round: Int) = LottoDraw(round = round, tickets = LottoTickets())

class LottoDrawsTest :
    WordSpec({
        "빈 LottoDraws" should {
            "isEmpty는 true, isNotEmpty는 false다" {
                LottoDraws().isEmpty() shouldBe true
                LottoDraws().isNotEmpty() shouldBe false
            }
            "lastRound는 0, last는 null이다" {
                LottoDraws().lastRound() shouldBe 0
                LottoDraws().last() shouldBe null
            }
            "updateLast는 그대로 빈 묶음을 반환한다" {
                LottoDraws().updateLast(draw(1)).isEmpty() shouldBe true
            }
        }

        "LottoDraws.append" should {
            "회차를 추가한 새 묶음을 반환한다" {
                val draws = LottoDraws().append(draw(1)).append(draw(2))
                draws.lastRound() shouldBe 2
                draws.isNotEmpty() shouldBe true
            }
        }

        "LottoDraws.findByRound" should {
            "해당 회차를 찾고, 없으면 null을 반환한다" {
                val draws = LottoDraws().append(draw(1)).append(draw(2))
                draws.findByRound(2)?.round shouldBe 2
                draws.findByRound(99) shouldBe null
            }
        }

        "LottoDraws.last" should {
            "마지막 회차를 반환한다" {
                LottoDraws()
                    .append(draw(1))
                    .append(draw(2))
                    .last()
                    ?.round shouldBe 2
            }
        }

        "LottoDraws.updateLast" should {
            "마지막 회차를 교체한다" {
                val draws = LottoDraws().append(draw(1)).append(draw(2)).updateLast(draw(5))
                draws.last()?.round shouldBe 5
            }
        }

        "LottoDraws.toDisplayString" should {
            "회차들을 줄바꿈으로 이어붙인다" {
                LottoDraws().append(draw(1)).toDisplayString() shouldContain "#1"
            }
        }
    })
