package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class GrantPointUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(request: GrantPointRequest) {

        val user = userService.getCurrentUser()
        val students = studentService.getStudentsWithPointHistory(request.studentIdList)

        val pointOption = pointService.getPointOptionById(request.pointOptionId, user.schoolId)

        val pointHistories = pointService.getPointHistoriesByStudentsAndPointOptionAndSchoolId(
            students = students,
            pointOption = pointOption,
            schoolId = user.schoolId
        )

        pointService.saveAllPointHistories(pointHistories)
    }
}
