package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryVerifiedStudentPort

@ReadOnlyUseCase
class CheckStudentGcnUseCase(
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryVerifiedStudentPort: StudentQueryVerifiedStudentPort
) {

    fun execute(request: CheckStudentGcnRequest): String {
        val school = querySchoolPort.querySchoolById(request.schoolId) ?: throw SchoolNotFoundException

        val verifiedStudent = queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(
            gcn = Student.processGcn(request.grade, request.classRoom, request.number),
            schoolName = school.name
        ) ?: throw VerifiedStudentNotFoundException

        return verifiedStudent.name
    }
}
