package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID
import team.aliens.dms.domain.tag.dto.TagResponse

@ReadOnlyUseCase
class QueryStudentsUseCase(
    private val userService: UserService,
    private val studentService: StudentService
) {

    fun execute(
        name: String?,
        sort: Sort,
        filterType: PointFilterType?,
        minPoint: Int?,
        maxPoint: Int?,
        tagIds: List<UUID>?
    ): StudentsResponse {

        val user = userService.getCurrentUser()

        val pointFilter = PointFilter(
            filterType = filterType,
            minPoint = minPoint,
            maxPoint = maxPoint
        )

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
