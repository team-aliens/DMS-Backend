package team.aliens.dms.domain.auth.service.impl

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.service.CheckAuthCodeService
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort

@Service
class CheckAuthCodeServiceImpl(
    private val queryAuthCodePort: QueryAuthCodePort
) : CheckAuthCodeService {

    override fun checkAuthCodeByEmailAndEmailType(email: String, type: EmailType, code: String) {
        (
        queryAuthCodePort.queryAuthCodeByEmailAndEmailType(email, type)
            ?: throw AuthCodeNotFoundException
            )
            .apply { validateAuthCode(code) }
    }
}
