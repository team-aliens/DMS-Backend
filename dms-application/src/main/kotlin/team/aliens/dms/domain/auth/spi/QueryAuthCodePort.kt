package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import java.util.UUID

interface QueryAuthCodePort {
    fun queryAuthCodeByUserIdAndType(userId: UUID, type: EmailType) : AuthCode?
}