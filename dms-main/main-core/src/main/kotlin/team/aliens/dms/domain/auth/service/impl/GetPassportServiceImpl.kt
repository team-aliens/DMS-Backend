package team.aliens.dms.domain.auth.service.impl

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.domain.auth.service.GetPassportService
import team.aliens.dms.domain.auth.spi.QueryPassportPort

@Service
class GetPassportServiceImpl(
    private val queryPassportPort: QueryPassportPort
) : GetPassportService {

    override fun getPassportByToken(token: String): Passport {
        return queryPassportPort.queryPassportByToken(token)
    }
}
