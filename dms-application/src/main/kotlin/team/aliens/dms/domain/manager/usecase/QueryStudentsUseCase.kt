package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.QueryStudentListResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.student.model.Student

@ReadOnlyUseCase
class QueryStudentsUseCase(
    private val queryStudentPort: ManagerQueryStudentPort
) {

    fun execute(name: String, sort: Sort): QueryStudentListResponse {
        val students = queryStudentPort.queryStudentsByNameAndSort(name, sort).map {
            QueryStudentListResponse.StudentElement(
                id = it.id,
                name = it.name,
                gcn = it.gcn,
                roomNumber = it.roomNumber,
                profileImageUrl = it.profileImageUrl ?: Student.PROFILE_IMAGE
            )
        }

        return QueryStudentListResponse(students)
    }
}