package team.aliens.dms.domain.user.service

interface CheckUserService {

    fun checkUserNotExistsByEmail(email: String)

    fun checkUserExistsByEmail(email: String)

    fun checkUserNotExistsByAccountId(accountId: String)
}
