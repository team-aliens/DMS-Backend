package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
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
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: StudentQueryUserPort
) {

    fun execute(schoolId: UUID, request: FindStudentAccountIdRequest): String {
        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId = schoolId,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        ) ?: throw StudentNotFoundException

        val user = queryUserPort.queryUserById(student.id) ?: throw UserNotFoundException

        if (student.name != request.name) {
            throw StudentInfoMismatchException
        }

        return StringUtil.coveredEmail(user.email)
    }
}