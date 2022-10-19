package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.student.dto.FindAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import team.aliens.dms.global.spi.CoveredEmailPort
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
    private val queryUserPort: StudentQueryUserPort,
    private val coveredEmailPort: CoveredEmailPort
) {

    fun execute(schoolId: UUID, request: FindAccountIdRequest): String {
        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId, request.grade, request.classRoom, request.number
        ) ?: throw StudentNotFoundException
        
        val user = queryUserPort.queryByUserId(student.studentId) ?: throw UserNotFoundException

        if (user.name != request.name) {
            throw StudentInfoNotMatchedException
        }

        return coveredEmailPort.coveredEmail(user.email)
    }
}