package lotto.repository

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

class JsonFileStorage<T>(
    private val path: String,
    private val serializer: KSerializer<T>,
    private val default: () -> T,
) {
    fun load(): T {
        val file = File(path)
        val data = file.takeIf { it.exists() }?.readText()

        return if (data == null) {
            default()
        } else {
            try {
                Json.decodeFromString(serializer, data)
            } catch (e: Exception) {
                val backup = File("$path.bak")
                file.renameTo(backup)
                default()
            }
        }
    }

    fun save(data: T) {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeText(Json.encodeToString(serializer, data))
    }
}
