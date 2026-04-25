package lotto.service

import lotto.repository.AccountRepository

class AccountService(
    private val accountRepository: AccountRepository,
) {
    fun getAmount(): Long = accountRepository.load().amount

    fun deposit(amount: Long) {
        val account = accountRepository.load()
        account.deposit(amount)
        accountRepository.save(account)
    }

    fun withdraw(amount: Long) {
        val account = accountRepository.load()
        account.withdraw(amount)
        accountRepository.save(account)
    }
}
