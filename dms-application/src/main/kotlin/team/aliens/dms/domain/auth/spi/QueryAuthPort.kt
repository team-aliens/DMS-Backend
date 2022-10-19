package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCode
import java.util.UUID

interface QueryAuthPort {
    fun queryAuthCodeByUserId(userId: UUID): AuthCode?
}