package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val schoolService: SchoolService,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(request: CheckStudentGcnRequest): String {
        val school = schoolService.getSchoolById(request.schoolId)

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
