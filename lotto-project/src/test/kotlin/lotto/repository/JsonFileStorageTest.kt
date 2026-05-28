package lotto.repository

import io.kotest.core.spec.style.WordSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.serializer
import java.io.File

private fun longStorage(path: String) =
    JsonFileStorage(
        path = path,
        serializer = Long.serializer(),
        default = { 0L },
    )

class JsonFileStorageTest :
    WordSpec({
        val dir = tempdir()

        fun pathOf(name: String) = File(dir, name).absolutePath

        "JsonFileStorage.load" should {
            "파일이 없으면 기본값을 반환한다" {
                longStorage(pathOf("absent.json")).load() shouldBe 0L
            }
            "저장된 값을 읽어온다" {
                val storage = longStorage(pathOf("roundtrip.json"))
                storage.save(42L)
                storage.load() shouldBe 42L
            }
            "손상된 파일은 백업하고 기본값을 반환한다" {
                val path = pathOf("broken.json")
                File(path).writeText("{ broken json")

                longStorage(path).load() shouldBe 0L
                File("$path.bak").exists() shouldBe true
            }
        }

        "JsonFileStorage.save" should {
            "상위 디렉터리가 없으면 생성한다" {
                val path = pathOf("nested/dir/data.json")
                val storage = longStorage(path)
                storage.save(7L)
                File(path).exists() shouldBe true
                storage.load() shouldBe 7L
            }
        }
    })
