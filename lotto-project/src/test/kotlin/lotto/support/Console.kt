package lotto.support

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun <T> withStdin(
    input: String,
    block: () -> T,
): T {
    val original = System.`in`
    System.setIn(ByteArrayInputStream(input.toByteArray(Charsets.UTF_8)))
    try {
        return block()
    } finally {
        System.setIn(original)
    }
}

fun captureStdout(block: () -> Unit): String {
    val original = System.out
    val buffer = ByteArrayOutputStream()
    System.setOut(PrintStream(buffer, true, "UTF-8"))
    try {
        block()
    } finally {
        System.setOut(original)
    }
    return buffer.toString("UTF-8")
}
