package team.aliens.dms.domain.auth.service.impl

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.service.CommandAuthCodeService
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodePort

@Service
class CommandAuthCodeServiceImpl(
    private val commandAuthCodePort: CommandAuthCodePort,
    private val commandAuthCodeLimitPort: CommandAuthCodeLimitPort
) : CommandAuthCodeService {

    override fun saveAuthCode(authCode: AuthCode) {
        commandAuthCodePort.saveAuthCode(authCode)
    }

    override fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit) {
        commandAuthCodeLimitPort.saveAuthCodeLimit(authCodeLimit)
    }
}
