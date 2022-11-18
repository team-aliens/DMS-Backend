package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.QueryStudentsResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort

@ReadOnlyUseCase
class QueryStudentsUseCase(
    private val queryStudentPort: ManagerQueryStudentPort
) {

    fun execute(name: String, sort: Sort): QueryStudentsResponse {
        val students = queryStudentPort.queryStudentsByNameAndSort(name, sort).map {
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