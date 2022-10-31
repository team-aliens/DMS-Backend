package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: StudentQueryUserPort
) {

    fun execute(request: CheckStudentGcnRequest): String {
        val school = querySchoolPort.querySchoolById(request.schoolId) ?: throw SchoolNotFoundException

        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId = school.id,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        ) ?: throw StudentNotFoundException

        val user =  queryUserPort.queryUserById(student.studentId) ?: throw UserNotFoundException

        return user.name
    }
}