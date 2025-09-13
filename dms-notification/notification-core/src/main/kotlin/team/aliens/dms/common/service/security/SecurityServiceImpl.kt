package team.aliens.dms.common.service.security

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort

@Service
class SecurityServiceImpl(
    private val securityPort: SecurityPort
) : SecurityService {

    override fun getCurrentUserId() =
        securityPort.getCurrentUserId()

    override fun getCurrentSchoolId() =
        securityPort.getCurrentUserSchoolId()
}
