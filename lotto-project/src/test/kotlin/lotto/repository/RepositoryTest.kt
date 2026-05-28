package lotto.repository

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.serializer
import lotto.model.Account
import lotto.model.LottoDraw
import lotto.model.LottoDraws
import lotto.model.LottoNumber
import lotto.model.LottoNumbers
import lotto.model.LottoTickets
import java.io.File
import java.nio.file.Files

private fun tempPath(name: String): String = File(Files.createTempDirectory("lotto-repo-test").toFile(), name).absolutePath

private fun accountStorage(path: String) = JsonFileStorage(path, Long.serializer(), default = { 0L })

private fun drawStorage(path: String) = JsonFileStorage(path, LottoDraws.serializer(), default = { LottoDraws() })

class AccountRepositoryTest :
    WordSpec({
        "AccountRepository" should {
            "저장된 값이 없으면 잔액 0인 계좌를 반환한다" {
                AccountRepository(accountStorage(tempPath("account.json"))).load().amount shouldBe 0L
            }
            "계좌를 저장하고 다시 불러온다" {
                val path = tempPath("account.json")
                val repository = AccountRepository(accountStorage(path))
                repository.save(Account(1500))
                repository.load().amount shouldBe 1500L
            }
        }
    })

class LottoDrawRepositoryTest :
    WordSpec({
        val draw = LottoDraw(round = 1, tickets = LottoTickets(listOf(LottoNumbers((1..6).map { LottoNumber(it) }))))

        "LottoDrawRepository" should {
            "초기에는 빈 회차 묶음을 반환한다" {
                LottoDrawRepository(drawStorage(tempPath("lotto.json"))).load().isEmpty() shouldBe true
            }
            "저장하면 메모리에서 즉시 조회된다" {
                val repository = LottoDrawRepository(drawStorage(tempPath("lotto.json")))
                repository.save(LottoDraws().append(draw))
                repository.load().lastRound() shouldBe 1
            }
            "저장 후 새 인스턴스는 파일에서 복원한다" {
                val path = tempPath("lotto.json")
                LottoDrawRepository(drawStorage(path)).save(LottoDraws().append(draw))
                LottoDrawRepository(drawStorage(path)).load().lastRound() shouldBe 1
            }
        }
    })
