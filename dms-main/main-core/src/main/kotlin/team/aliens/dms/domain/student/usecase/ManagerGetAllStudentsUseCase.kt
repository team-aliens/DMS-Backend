package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerGetAllStudentsUseCase(
    private val userService: UserService,
    private val studentService: StudentService
) {

    fun execute(
        name: String?,
        sort: Sort,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): StudentsResponse {
        val user = userService.getCurrentUser()

        val students = studentService.getStudentsByNameAndSortAndFilter(
            name = name,
            sort = sort,
            schoolId = user.schoolId,
            pointFilter = pointFilter,
            tagIds = tagIds
        )

        return StudentsResponse.of(students)
    }
}
