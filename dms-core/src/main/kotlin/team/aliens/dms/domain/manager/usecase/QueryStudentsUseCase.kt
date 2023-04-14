package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.QueryStudentsResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentsUseCase(
    private val getUserService: GetUserService,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(
        name: String?,
        sort: Sort,
        filterType: PointFilterType?,
        minPoint: Int?,
        maxPoint: Int?,
        tagIds: List<UUID>?
    ): QueryStudentsResponse {

        val user = getUserService.getCurrentUser()

        val pointFilter = PointFilter(
            filterType = filterType,
            minPoint = minPoint,
            maxPoint = maxPoint
        )

        val students = queryStudentPort.queryStudentsByNameAndSortAndFilter(
            name = name,
            sort = sort,
            schoolId = user.schoolId,
            pointFilter = pointFilter,
            tagIds = tagIds
        ).map {
            QueryStudentsResponse.StudentElement(
                id = it.id,
                name = it.name,
                gcn = it.gcn,
                roomNumber = it.roomNumber,
                profileImageUrl = it.profileImageUrl,
                sex = it.sex,
                tags = it.tags
                    .map {
                            tag ->
                        QueryStudentsResponse.StudentTagElement(
                            id = tag.id,
                            name = tag.name,
                            color = tag.color
                        )
                    }
            )
        }

        return QueryStudentsResponse(students)
    }
}
