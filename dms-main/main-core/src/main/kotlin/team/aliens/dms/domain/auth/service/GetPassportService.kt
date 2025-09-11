package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.Passport

interface GetPassportService {
    fun getPassportByToken(token: String): Passport
}
