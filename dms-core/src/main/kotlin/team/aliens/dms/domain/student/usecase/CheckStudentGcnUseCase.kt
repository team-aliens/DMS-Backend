package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val schoolService: SchoolService,
    private val studentService: StudentService
) {

    fun execute(request: CheckStudentGcnRequest): String {
        val school = schoolService.getSchoolById(request.schoolId)

        val student = studentService.getStudentBySchoolIdAndGcn(
            schoolId = school.id,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        )

        if (student.hasUser) {
            throw StudentAlreadyExistsException
        }

        return student.name
    }
}
