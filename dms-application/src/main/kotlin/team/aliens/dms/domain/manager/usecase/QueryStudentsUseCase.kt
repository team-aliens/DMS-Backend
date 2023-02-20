package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.QueryStudentsResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.point.exception.InvalidFilterRequestException

@ReadOnlyUseCase
class QueryStudentsUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryManagerPort: QueryManagerPort,
    private val queryStudentPort: ManagerQueryStudentPort
) {

    fun execute(name: String?, sort: Sort, filterType: String?, minPoint: Int?, maxPoint: Int?): QueryStudentsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val pointFilterType = if(filterType != null) {
            if(maxPoint == null || minPoint == null) {
                throw InvalidFilterRequestException
            }

            PointFilterType.valueOf(filterType)
        } else {
            null
        }

        val students = queryStudentPort.queryStudentsByNameAndSortAndFilter(
            name, sort, manager.schoolId, pointFilterType, minPoint, maxPoint
        ).map {
            QueryStudentsResponse.StudentElement(
                id = it.id,
                name = it.name,
                gcn = it.gcn,
                roomNumber = it.roomNumber,
                profileImageUrl = it.profileImageUrl!!
            )
        }

        return QueryStudentsResponse(students)
    }
}