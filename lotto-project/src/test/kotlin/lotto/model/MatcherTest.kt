package lotto.model

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

private fun ticket(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

class MatcherTest :
    WordSpec({
        val winning = WinningNumbers(ticket(1, 2, 3, 4, 5, 6), LottoNumber(7))
        val matcher = Matcher(winning)

        "Matcher.groupByRank" should {
            "각 티켓을 등수별로 집계한다" {
                val tickets =
                    LottoTickets(
                        listOf(
                            ticket(1, 2, 3, 4, 5, 6),
                            ticket(1, 2, 3, 4, 5, 7),
                            ticket(1, 2, 3, 4, 5, 8),
                            ticket(40, 41, 42, 43, 44, 45),
                        ),
                    )

                val result = matcher.groupByRank(tickets)

                result[LottoRank.FIRST] shouldBe 1
                result[LottoRank.SECOND] shouldBe 1
                result[LottoRank.THIRD] shouldBe 1
                result[LottoRank.LOSE] shouldBe 1
            }
            "같은 등수는 합산한다" {
                val tickets =
                    LottoTickets(
                        listOf(
                            ticket(1, 2, 3, 8, 9, 10),
                            ticket(1, 2, 3, 11, 12, 13),
                        ),
                    )

                matcher.groupByRank(tickets)[LottoRank.FIFTH] shouldBe 2
            }
            "티켓이 없으면 빈 결과를 반환한다" {
                matcher.groupByRank(LottoTickets()) shouldBe emptyMap()
            }
        }
    })
