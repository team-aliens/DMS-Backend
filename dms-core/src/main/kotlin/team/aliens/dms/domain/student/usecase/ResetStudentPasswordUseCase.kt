package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class ResetStudentPasswordUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val queryAuthCodePort: QueryAuthCodePort,
    private val securityService: SecurityService
) {

    fun execute(request: ResetStudentPasswordRequest) {
        val user = userService.queryUserByAccountId(request.accountId)
        val student = studentService.getStudentByUserId(user.id)

        if (student.name != request.name || user.email != request.email) {
            throw StudentInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(user.email) ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        userService.saveUser(
            user.copy(password = securityService.encodePassword(request.newPassword))
        )
    }
}
