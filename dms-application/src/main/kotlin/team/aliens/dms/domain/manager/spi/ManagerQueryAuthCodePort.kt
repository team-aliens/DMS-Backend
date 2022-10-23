package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.auth.model.AuthCode

interface ManagerQueryAuthCodePort {
    fun queryAuthCodeByEmail(email: String): AuthCode?
}