package lotto.model

class Account(
    amount: Long,
) {
    var amount: Long = amount
        private set

    fun withdraw(amount: Long) {
        require(amount > 0) { "출금액은 0보다 커야 합니다." }
        require(this.amount >= amount) { "잔액보다 출금액이 많습니다." }
        this.amount -= amount
    }

    fun deposit(amount: Long) {
        require(amount > 0) { "입금액은 0보다 커야 합니다." }
        this.amount += amount
    }
}
