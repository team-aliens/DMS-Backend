package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.model.AuthCodeLimit

interface StudentQueryAuthCodeLimitPort {
    fun queryAuthCodeLimitByEmail(email: String): AuthCodeLimit?
}