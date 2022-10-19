package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.student.dto.StudentPasswordInitializationRequest
import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.spi.UserSecurityPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class StudentPasswordInitializationUseCase(
    private val queryUserPort: StudentQueryUserPort,
    private val studentQueryAuthPort: StudentQueryAuthPort,
    private val studentCommandUserPort: StudentCommandUserPort,
    private val userSecurityPort: UserSecurityPort
) {

    fun execute(request: StudentPasswordInitializationRequest) {
        val user = queryUserPort.queryByAccountId(request.accountId) ?: throw StudentNotFoundException

        if (request.name != user.name || request.email != user.email) {
            throw StudentInfoNotMatchedException
        }

        val authCode = studentQueryAuthPort.queryAuthCodeByUserId(user.id) ?: throw AuthCodeNotFoundException

        if (request.authCode != authCode.code) {
            throw AuthCodeNotMatchedException
        }

        studentCommandUserPort.saveUser(
            user.copy(password = userSecurityPort.encode(request.newPassword))
        )
    }
}