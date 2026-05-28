package lotto.util

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class ExtensionsTest :
    WordSpec({
        "Long.toFormattedString" should {
            "억 단위는 억/만/나머지를 모두 표기한다" {
                123_456_789L.toFormattedString() shouldBe "1억 2345만 6,789"
            }
            "억 단위에서 만/나머지가 0이면 억만 표기한다" {
                2_100_000_000L.toFormattedString() shouldBe "21억"
            }
            "만 단위는 만/나머지를 표기한다" {
                12_345L.toFormattedString() shouldBe "1만 2,345"
            }
            "만 단위에서 나머지가 0이면 만만 표기한다" {
                50_000L.toFormattedString() shouldBe "5만"
            }
            "만 미만은 천단위 콤마로 표기한다" {
                5_000L.toFormattedString() shouldBe "5,000"
            }
        }

        "Int.toFormattedString" should {
            "Long 포맷과 동일하게 동작한다" {
                5_000.toFormattedString() shouldBe "5,000"
            }
        }

        "String.padEndKo" should {
            "한글은 2칸으로 계산해 폭을 보정한다" {
                "회차".padEndKo(6).length shouldBe 4
            }
            "영문만 있으면 일반 padEnd와 같다" {
                "abc".padEndKo(6) shouldBe "abc   "
            }
        }
    })
