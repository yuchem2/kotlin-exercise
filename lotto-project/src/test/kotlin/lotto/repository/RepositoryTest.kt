package lotto.repository

import io.kotest.core.spec.style.WordSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import lotto.model.Account
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.model.LottoRank
import lotto.model.LottoTickets
import lotto.model.WinningNumbers
import java.io.File

private fun accountStorage(path: String) = JsonFileStorage(path, Long.serializer(), default = { 0L })

private fun drawStorage(path: String) = JsonFileStorage(path, LottoDraws.serializer(), default = { LottoDraws() })

private fun fixtureFile(name: String): File = File(object {}.javaClass.getResource("/fixtures/$name")!!.toURI())

private fun numbers(vararg values: Int) = LottoNumbers(values.map { LottoNumber(it) })

/** 두 JSON 문자열을 포맷/공백/키 순서와 무관하게 구조로 비교한다. */
private infix fun String.shouldBeSameJsonAs(expected: String) = Json.parseToJsonElement(this) shouldBe Json.parseToJsonElement(expected)

class AccountRepositoryTest :
    WordSpec({
        val dir = tempdir()

        fun pathOf(name: String) = File(dir, name).absolutePath

        // 읽기 전용 fixture를 tempdir 사본으로 staging해 원본 리소스를 보호하고 테스트 간 격리한다.
        fun stagedFixture(name: String) = File(dir, name).apply { writeText(fixtureFile(name).readText()) }.absolutePath

        "AccountRepository" should {
            "저장된 값이 없으면 잔액 0인 계좌를 반환한다" {
                AccountRepository(accountStorage(pathOf("empty.json"))).load().amount shouldBe 0L
            }
            "fixture JSON을 역직렬화해 잔액을 복원한다" {
                AccountRepository(accountStorage(stagedFixture("account-fixture.json"))).load().amount shouldBe 1500L
            }
            "계좌를 저장하면 fixture와 동일한 JSON 포맷이 된다" {
                val path = pathOf("account-save.json")
                AccountRepository(accountStorage(path)).save(Account(1500))

                File(path).readText() shouldBeSameJsonAs fixtureFile("account-fixture.json").readText()
            }
        }
    })

class LottoDrawRepositoryTest :
    WordSpec({
        val dir = tempdir()

        fun pathOf(name: String) = File(dir, name).absolutePath

        // 읽기 전용 fixture를 tempdir 사본으로 staging해 원본 리소스를 보호하고 테스트 간 격리한다.
        fun stagedFixture(name: String) = File(dir, name).apply { writeText(fixtureFile(name).readText()) }.absolutePath

        // fixture(lotto-draws.json)와 동일한 종료 회차: 당첨번호와 티켓이 6개 모두 일치 → 1등 1개
        val endedDraws =
            LottoDraws().append(
                LottoDraw(round = 1, tickets = LottoTickets(listOf(numbers(1, 2, 3, 4, 5, 6)))).apply {
                    endDraw(WinningNumbers(numbers(1, 2, 3, 4, 5, 6), LottoNumber(7)))
                },
            )

        "LottoDrawRepository" should {
            "초기에는 빈 회차 묶음을 반환한다" {
                LottoDrawRepository(drawStorage(pathOf("empty.json"))).load().isEmpty() shouldBe true
            }
            "저장하면 메모리에서 즉시 조회된다" {
                val repository = LottoDrawRepository(drawStorage(pathOf("memory.json")))
                repository.save(endedDraws)
                repository.load().lastRound() shouldBe 1
            }
            "fixture JSON을 역직렬화해 종료된 회차 결과를 복원한다" {
                val loaded = LottoDrawRepository(drawStorage(stagedFixture("lotto-draws-fixture.json"))).load()

                loaded.lastRound() shouldBe 1
                val restored = loaded.findByRound(1)!!
                restored.isEnded() shouldBe true
                restored.getResult().result[LottoRank.FIRST] shouldBe 1
                restored.getResult().totalIncome shouldBe 2_100_000_000L
            }
            "종료된 회차를 저장하면 fixture와 동일한 JSON 포맷이 된다" {
                val path = pathOf("draws-save.json")
                LottoDrawRepository(drawStorage(path)).save(endedDraws)

                File(path).readText() shouldBeSameJsonAs fixtureFile("lotto-draws-fixture.json").readText()
            }
        }
    })
