package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class ResetStudentPasswordUseCase(
    private val queryUserPort: StudentQueryUserPort,
    private val queryAuthCodePort: StudentQueryAuthCodePort,
    private val commandUserPort: StudentCommandUserPort,
    private val securityPort: StudentSecurityPort
) {

    fun execute(request: ResetStudentPasswordRequest) {
        val user = queryUserPort.queryByAccountId(request.accountId) ?: throw StudentNotFoundException

        if (request.name != user.name || request.email != user.email) {
            throw StudentInfoNotMatchedException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(user.email) ?: throw AuthCodeNotFoundException

        if (request.authCode != authCode.code) {
            throw AuthCodeNotMatchedException
        }

        commandUserPort.saveUser(
            user.copy(password = securityPort.encode(request.newPassword))
        )
    }
}