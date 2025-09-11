package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.Passport

interface QueryPassportPort {

    fun queryPassportByToken(token: String): Passport
}
