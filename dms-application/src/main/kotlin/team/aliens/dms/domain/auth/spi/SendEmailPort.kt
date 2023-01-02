package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.EmailType

interface SendEmailPort {

    fun sendAuthCode(email: String, type: EmailType, code: String)

    fun sendAccountId(email: String, accountId: String)

}