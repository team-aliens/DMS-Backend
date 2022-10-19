package team.aliens.dms.domain.student.spi

interface StudentQueryUserPort {
    fun existsByEmail(email: String): Boolean
    fun existsByAccountId(accountId: String): Boolean
}