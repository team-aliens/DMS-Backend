package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.EmailType

interface SendAuthPort {
    fun sendEmailCode(email: String, type: EmailType, code: String)
}