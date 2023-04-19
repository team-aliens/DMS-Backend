package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class GrantPointUseCase(
    private val userService: UserService,
    private val pointService: PointService,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(request: GrantPointRequest) {

        val user = userService.getCurrentUser()

        val pointOption = pointService.getPointOptionById(request.pointOptionId, user.schoolId)

        val students = queryStudentPort.queryStudentsWithPointHistory(request.studentIdList)

        if (students.size != request.studentIdList.size) {
            throw StudentNotFoundException
        }

        val pointHistories = pointService.getPointHistoriesByStudentsAndPointOptionAndSchoolId(
            students = students,
            pointOption = pointOption,
            schoolId = user.schoolId
        )

        pointService.saveAllPointHistories(pointHistories)
    }
}
