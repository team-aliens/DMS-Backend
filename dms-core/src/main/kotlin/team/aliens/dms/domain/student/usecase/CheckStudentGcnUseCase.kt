package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(request: CheckStudentGcnRequest): String {
        val school = querySchoolPort.querySchoolById(request.schoolId) ?: throw SchoolNotFoundException

        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId = school.id,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        ) ?: throw StudentNotFoundException

        if (student.userId != null) {
            throw StudentAlreadyExistsException
        }

        return student.name
    }
}
