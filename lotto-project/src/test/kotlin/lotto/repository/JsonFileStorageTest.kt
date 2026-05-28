package lotto.repository

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.serializer
import java.io.File
import java.nio.file.Files

private fun tempDir(): File = Files.createTempDirectory("lotto-storage-test").toFile()

private fun longStorage(path: String) =
    JsonFileStorage(
        path = path,
        serializer = Long.serializer(),
        default = { 0L },
    )

class JsonFileStorageTest :
    WordSpec({
        "JsonFileStorage.load" should {
            "파일이 없으면 기본값을 반환한다" {
                val path = File(tempDir(), "data.json").absolutePath
                longStorage(path).load() shouldBe 0L
            }
            "저장된 값을 읽어온다" {
                val path = File(tempDir(), "data.json").absolutePath
                val storage = longStorage(path)
                storage.save(42L)
                storage.load() shouldBe 42L
            }
            "손상된 파일은 백업하고 기본값을 반환한다" {
                val dir = tempDir()
                val file = File(dir, "data.json")
                file.writeText("{ broken json")

                longStorage(file.absolutePath).load() shouldBe 0L
                File("${file.absolutePath}.bak").exists() shouldBe true
            }
        }

        "JsonFileStorage.save" should {
            "상위 디렉터리가 없으면 생성한다" {
                val path = File(tempDir(), "nested/dir/data.json").absolutePath
                val storage = longStorage(path)
                storage.save(7L)
                File(path).exists() shouldBe true
                storage.load() shouldBe 7L
            }
        }
    })
