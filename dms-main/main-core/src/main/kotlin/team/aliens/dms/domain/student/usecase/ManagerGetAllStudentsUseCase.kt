package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerGetAllStudentsUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(
        name: String?,
        sort: Sort,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): StudentsResponse {
        val user = userService.getCurrentUser()

        val students = studentService.getStudentsByNameAndSortAndTag(
            name = name,
            sort = sort,
            schoolId = user.schoolId,
            tagIds = tagIds
        )

        val pointTotals = pointService.getPointTotalsGroupByStudent(user.schoolId)
            .associateBy { it.studentId }

        val studentsWithPoint = students
            .map { student ->
                pointTotals[student.id]
                    ?.let { student.copy(bonusPoint = it.bonusTotal, minusPoint = it.minusTotal) }
                    ?: student
            }
            .filter { pointFilter.matches(it.bonusPoint, it.minusPoint) }

        return StudentsResponse.of(studentsWithPoint)
    }
}
