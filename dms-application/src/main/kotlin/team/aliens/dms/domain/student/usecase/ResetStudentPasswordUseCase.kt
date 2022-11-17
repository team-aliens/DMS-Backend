package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority

@UseCase
class ResetStudentPasswordUseCase(
    private val queryUserPort: StudentQueryUserPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryAuthCodePort: StudentQueryAuthCodePort,
    private val commandUserPort: StudentCommandUserPort,
    private val securityPort: StudentSecurityPort,
    private val checkUserAuthority: CheckUserAuthority
) {

    fun execute(request: ResetStudentPasswordRequest) {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentById(user.id) ?: throw StudentNotFoundException

        if (checkUserAuthority.execute(user.id) != Authority.STUDENT) {
            throw StudentNotFoundException
        }

        if (student.name != request.name || user.email != request.email) {
            throw StudentInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(user.email) ?: throw AuthCodeNotFoundException

        if (authCode.code != request.authCode) {
            throw AuthCodeMismatchException
        }

        commandUserPort.saveUser(
            user.copy(password = securityPort.encodePassword(request.newPassword))
        )
    }
}