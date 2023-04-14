package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.CommandUserService
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class ResetStudentPasswordUseCase(
    private val getUserService: GetUserService,
    private val commandUserService: CommandUserService,
    private val queryStudentPort: QueryStudentPort,
    private val queryAuthCodePort: QueryAuthCodePort,
    private val securityService: SecurityService
) {

    fun execute(request: ResetStudentPasswordRequest) {
        val user = getUserService.queryUserByAccountId(request.accountId)
        val student = queryStudentPort.queryStudentByUserId(user.id) ?: throw StudentNotFoundException

        if (student.name != request.name || user.email != request.email) {
            throw StudentInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(user.email) ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        commandUserService.saveUser(
            user.copy(password = securityService.encodePassword(request.newPassword))
        )
    }
}
