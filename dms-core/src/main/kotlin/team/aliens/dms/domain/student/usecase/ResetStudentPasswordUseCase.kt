package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class ResetStudentPasswordUseCase(
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryAuthCodePort: QueryAuthCodePort,
    private val commandUserPort: CommandUserPort,
    private val securityService: SecurityService
) {

    fun execute(request: ResetStudentPasswordRequest) {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentByUserId(user.id) ?: throw StudentNotFoundException

        if (student.name != request.name || user.email != request.email) {
            throw StudentInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(user.email) ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        commandUserPort.saveUser(
            user.copy(password = securityService.encodePassword(request.newPassword))
        )
    }
}
