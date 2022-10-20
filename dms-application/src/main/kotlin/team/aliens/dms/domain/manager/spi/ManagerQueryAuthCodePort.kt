package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.auth.model.AuthCode
import java.util.UUID

interface ManagerQueryAuthCodePort {
    fun queryAuthCodeByUserId(userId: UUID): AuthCode?
}