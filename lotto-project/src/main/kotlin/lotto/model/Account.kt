package lotto.model

class Account(
    private var amount: Int
) {
    fun withdraw(amount: Int) {
        if (this.amount < amount) {
            throw IllegalArgumentException("잔액보다 출금액이 많습니다.")
        }
        this.amount -= amount
    }

    fun deposit(amount: Int) {
        this.amount += amount
    }

    fun getAmount() = amount
}
