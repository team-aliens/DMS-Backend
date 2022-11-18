package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort

@ReadOnlyUseCase
class StudentMyPageUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryPointPort: StudentQueryPointPort
) {

    fun execute(): StudentMyPageResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
        val school = querySchoolPort.querySchoolById(student.schoolId) ?: throw SchoolNotFoundException

        val bonusPoint = queryPointPort.queryTotalBonusPoint(student.id)
        val minusPoint = queryPointPort.queryTotalMinusPoint(student.id)

        return StudentMyPageResponse(
            schoolName = school.name,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl!!,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = "잘하자" // TODO 상벌점 상태에 따라서 문구 출력
        )
    }
}