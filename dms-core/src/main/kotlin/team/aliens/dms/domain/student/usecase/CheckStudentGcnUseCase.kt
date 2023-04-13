package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryVerifiedStudentPort

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val queryVerifiedStudentPort: QueryVerifiedStudentPort
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
