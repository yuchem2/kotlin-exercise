package lotto.util

fun Int.toFormattedString(): String {
    val billion = 100_000_000
    val million = 10_000

    val billionPart = this / billion
    val millionPart = this % billion / million
    val remainder = this % million

    return when {
        this >= billion -> {
            buildString {
                append("${billionPart}억 ")
                if (millionPart > 0) append("${millionPart}만 ")
                if (remainder > 0) append(String.format("%,d", remainder))
            }.trim()
        }

        this >= million -> {
            buildString {
                append("${millionPart}만 ")
                if (remainder > 0) append(String.format("%,d", remainder))
            }.trim()
        }

        else -> String.format("%,d", this)
    }
}
