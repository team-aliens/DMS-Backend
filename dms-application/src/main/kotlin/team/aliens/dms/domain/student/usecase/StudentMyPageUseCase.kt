package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointHistoryPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

@ReadOnlyUseCase
class StudentMyPageUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: StudentQueryUserPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryPointHistoryPort: StudentQueryPointHistoryPort
) {

    fun execute(): StudentMyPageResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
        val studentUser = queryUserPort.queryUserById(student.studentId) ?: throw UserNotFoundException
        val school = querySchoolPort.querySchoolById(student.schoolId) ?: throw SchoolNotFoundException

        val bonusPoint = queryPointHistoryPort.queryTotalBonusPoint(student.studentId)
        val minusPoint = queryPointHistoryPort.queryTotalMinusPoint(student.studentId)

        return StudentMyPageResponse(
            schoolName = school.name,
            name = studentUser.name,
            gcn = student.gcn,
            profileImageUrl = studentUser.profileImageUrl ?: User.PROFILE_IMAGE,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = "잘하자" // TODO 상벌점 상태에 따라서 문구 출력
        )
    }
}