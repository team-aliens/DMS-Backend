package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

/**
 *
 * 학생의 이름이 소속된 학교의 학생 이름과 일치한다면 블록처리된 이메일을 반환하는 FindStudentAccountIdUseCase
 *
 * @author leejeongyoon
 * @date 2022/10/19
 * @version 1.0.0
 **/
@ReadOnlyUseCase
class FindStudentAccountIdUseCase(
    private val studentService: StudentService,
    private val userService: UserService,
    private val sendEmailPort: SendEmailPort
) {

    fun execute(schoolId: UUID, request: FindStudentAccountIdRequest): String {
        val student = studentService.queryStudentBySchoolIdAndGcn(
            schoolId = schoolId,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        )

        val user = student.userId?.let {
            userService.queryUserById(it)
        } ?: throw UserNotFoundException

        if (student.name != request.name) {
            throw StudentInfoMismatchException
        }

        sendEmailPort.sendAccountId(user.email, user.accountId)

        return StringUtil.coveredEmail(user.email)
    }
}
