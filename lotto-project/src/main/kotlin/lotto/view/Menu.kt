package lotto.view

enum class Menu(
    val number: Int,
    val description: String,
) {
    DEPOSIT(1, "입금"),
    PURCHASE(2, "로또 구매"),
    DRAW(3, "추첨"),
    HISTORY(4, "회차 조회"),
    ACCOUNT(5, "계좌 조회"),
    EXIT(0, "종료"),
    ;

    companion object {
        fun from(number: Int): Menu = entries.find { it.number == number } ?: throw IllegalArgumentException("없는 메뉴입니다.")
    }
}